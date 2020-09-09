package com.example.adminmodule.Repository

import android.annotation.SuppressLint
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.R
import com.example.adminmodule.UI.Adapters.BookedSeatsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookedSeatsRepo {
    private lateinit var reasonReference: DatabaseReference
    var dataReference = FirebaseDatabase.getInstance().getReference("Booking")
    var userNameList = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    var emoName = ""

    fun getUserNameList(
        userList: ArrayList<BookingModel>,
        listView: ListView,
        activity: FragmentActivity,
        iAddonCompleteListener: IAddonCompleteListener,
        userUid: String
    ) {
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val adapter: BookedSeatsAdapter?
                    for (item in snapshot.children) {
                        val userId = item.child("uid").value.toString()
                        if (userId == userUid) {
                            val userName = item.child("empName").value.toString()
                            userNameList.add(userName)
                        }
                    }
                    for (ind in 0..userNameList.size - 1) {
                        userList[ind].empName = userNameList[ind]
                    }
                    adapter = activity?.let {
                        BookedSeatsAdapter(
                            it,
                            R.layout.book_seat_item,
                            userList
                        )
                    }
                    listView.adapter = adapter!!
                    adapter.notifyDataSetChanged()
                    iAddonCompleteListener!!.addOnCompleteListener()
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    fun showUserCurrentBookingList(
        userList: ArrayList<BookingModel>,
        listView: ListView,
        activity: FragmentActivity,
        iAddonCompleteListener: IAddonCompleteListener
    ) {

        auth = FirebaseAuth.getInstance()
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("d-MM-yyyy")
        var currentDate = dateFormat.format(calendarInstance)

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var adapter: BookedSeatsAdapter?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = Integer.parseInt(item.child("booked").value.toString())
                        var chooseDate = item.child("date").value.toString()
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val date1 = sdf.parse(chooseDate)
                        val date2 = sdf.parse(currentDate)
                        if (isBooked == 0) {
                            val infoUser = item.getValue(BookingModel::class.java)!!
                            userList.add(infoUser)
                            getUserNameList(
                                userList,
                                listView,
                                activity,
                                iAddonCompleteListener,
                                userUid
                            )
                        }
                    }
                }
            }
        })
    }

    fun cancelUserBookingSeat(info: BookingModel, iAddonCompleteListener: IAddonCompleteListener) {
        auth = FirebaseAuth.getInstance()

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val userUid = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    if (info.id == userUid && bookedDate == info.date) {
                        val selectedUserUid = item.child("id").value.toString()
                        val selectedBookedDate = item.child("date").value.toString()
                        val isBooked = Integer.parseInt(item.child("booked").value.toString())

                        if (isBooked == 0) {
                            val checkInTime = item.child("checkInTime").value.toString()
                            val checkOutTime = item.child("checkOutTime").value.toString()
                            val reason = item.child("reason").value.toString()

                            dataReference.child(item.key.toString()).setValue(
                                BookingData(
                                    1,
                                    checkInTime,
                                    checkOutTime,
                                    selectedBookedDate,
                                    selectedUserUid,
                                    reason,
                                    status = "Canceled"
                                )
                            )
                            iAddonCompleteListener.addOnCompleteListener()
                        }
                    }

                }
            }
        })
    }

    fun updateSeatDataOnCancel(dateToCome: String) {
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
                                    Integer.parseInt(bookedSeat) - 1,
                                    totalSeats.toInt(),
                                    Integer.parseInt(availableSeat) + 1,
                                    dateToCome
                                )
                            )
                    }
                }
            }
        })
    }

}