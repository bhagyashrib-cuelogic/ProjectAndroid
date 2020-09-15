package com.example.adminmodule.Repository

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.Utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ReserveSeatRepo {
    private lateinit var reasonReference: DatabaseReference
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var auth: FirebaseAuth

    var selectedUserId = ""

    fun getReasonData(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        spinnerDataList.add(item.value.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun getUserData(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val userName = item.child("empName").value.toString()
                        spinnerDataList.add(userName)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun getSelectedUserId(
        activity: Activity,
        selectedDate: String,
        checkTime: String,
        checkOut: String,
        reason: String,
        user: String,
        iAddonCompleteListener: IAddonCompleteListener
    ) {

        auth = FirebaseAuth.getInstance()
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val userName = item.child("empName").value.toString()
                        if (userName == user) {
                            selectedUserId = item.child("uid").value.toString()
                            reserveSeats(
                                activity,
                                selectedDate,
                                checkTime,
                                checkOut,
                                reason,
                                selectedUserId,
                                iAddonCompleteListener
                            )
                            break
                        }
                    }
                }
            }
        })

    }

    private fun reserveSeats(
        activity: Activity,
        selectedDate: String,
        checkTime: String,
        checkOut: String,
        reason: String,
        user: String,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        Log.e("ReserveSeats", "UserId: $user")
        var isBooked: Boolean = true
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val bookedUserId = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isDeleted = item.child("booked").value.toString()
                    if (selectedDate == bookedDate && bookedUserId == user && Integer.parseInt(
                            isDeleted
                        ) == 0
                    ) {
                        Utils.showToast("You already booked a seat for this day", activity)
                        isBooked = false
                        break
                    }
                }
                if (isBooked) {
                    checkSeatAvailability(
                        user,
                        checkTime,
                        checkOut,
                        selectedDate,
                        reason,
                        activity,
                        iAddonCompleteListener
                    )
                }

            }
        })
    }

    private fun checkSeatAvailability(
        user: String,
        checkTime: String,
        checkOut: String,
        dateToCome: String,
        reason: String,
        activity: Activity,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")
            .orderByChild("date")
            .equalTo(dateToCome)

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var bookedSeat = ""
                var totalSeats = ""
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        bookedSeat = item.child("booked").value.toString()
                        totalSeats = item.child("total").value.toString()
                    }
                    if (totalSeats == bookedSeat) {
                        Utils.showToast("All Seats are booked for date $dateToCome", activity)
                    } else {
                        reserveSeatForEmp(
                            user,
                            checkTime,
                            checkOut,
                            dateToCome,
                            reason,
                            activity,
                            iAddonCompleteListener
                        )
                    }

                } else {
                    Utils.showToast("No Seats available for $dateToCome", activity)
                }
            }
        })

    }


    private fun reserveSeatForEmp(
        user: String,
        checkTime: String,
        checkOut: String,
        dateToCome: String,
        reason: String,
        activity: Activity,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        auth = FirebaseAuth.getInstance()

        if (dateToCome.isNotEmpty() && reason.isNotEmpty()) {
            val uidKey = firebaseReference.push().key!!
            firebaseReference.child(uidKey).setValue(
                BookingData(
                    0,
                    checkTime,
                    checkOut,
                    dateToCome,
                    user,
                    reason,
                    status = "Booked"
                )
            ).addOnCompleteListener {
                updateSeatTable(dateToCome)
                Utils.showToast("Your seat is booked", activity)
                iAddonCompleteListener.addOnCompleteListener()
            }
        }
    }

    private fun updateSeatTable(dateToCome: String) {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")
            .orderByChild("date")
            .equalTo(dateToCome)

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val bookedSeat = item.child("booked").value.toString()
                        val availableSeat = item.child("available").value.toString()
                        val totalSeats = item.child("total").value.toString()
                        firebaseReference.ref.child(item.key.toString())
                            .setValue(
                                SeatData(
                                    Integer.parseInt(bookedSeat) + 1,
                                    totalSeats.toInt(),
                                    Integer.parseInt(availableSeat) - 1,
                                    dateToCome
                                )
                            )

                    }
                }
            }
        })
    }


}