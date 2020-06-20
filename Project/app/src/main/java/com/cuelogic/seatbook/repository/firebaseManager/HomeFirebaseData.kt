package com.cuelogic.seatbook.repository.firebaseManager

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class HomeFirebaseData {


    private lateinit var auth: FirebaseAuth
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var reasonReference: DatabaseReference

    fun saveDate(
        checkTime: String,
        checkOut: String,
        reasonDescription: String,
        dateToCome: String,
        activity: FragmentActivity,
        iAddonCompleteListener: IAddonCompleteListener
    ) {

        auth = FirebaseAuth.getInstance()
        val currentUserUid = auth.currentUser!!.uid

        if (dateToCome.isNotEmpty() && reasonDescription.isNotEmpty()) {
            val uidKey = firebaseReference.push().key!!
            firebaseReference.child(uidKey).setValue(
                BookingData(
                    currentUserUid,
                    dateToCome,
                    checkTime,
                    checkOut,
                    reasonDescription,
                    "Booked",
                    0
                )
            ).addOnCompleteListener {
                Toast.makeText(activity, "Your seat is booked", Toast.LENGTH_SHORT).show()
                showSeatDateWise(dateToCome, 1)
                iAddonCompleteListener.addOnCompleteListener()
            }
        }
    }

    fun checkBookedSeatForParticularDateByUser(
        activity: FragmentActivity,
        dateToCome: String,
        checkInTime: String,
        checkOutTime: String,
        reasonDescription: String,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        var isBooked: Boolean = true
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser!!.uid

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val bookedUserId = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isDeleted = item.child("booked").value.toString()
                    if (dateToCome == bookedDate && bookedUserId == currentUserId && Integer.parseInt(
                            isDeleted
                        ) == 0
                    ) {
                        Toast.makeText(
                            activity,
                            "You are already booked for this day",
                            Toast.LENGTH_LONG
                        ).show()
                        isBooked = false
                        break
                    }
                }
                if (isBooked) saveDate(
                    checkInTime,
                    checkOutTime,
                    reasonDescription,
                    dateToCome,
                    activity,
                    iAddonCompleteListener
                )
            }
        })
    }

    fun showSeatDateWise(dateToCome: String, flag: Int): MutableLiveData<List<SeatData?>?> {
        val mutableLiveData: MutableLiveData<List<SeatData?>?> = MutableLiveData<List<SeatData?>?>()

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
                        if (flag == 1) {
                            firebaseReference.ref.child(item.key.toString())
                                .setValue(
                                    SeatData(
                                        Integer.parseInt(bookedSeat) + 1,
                                        200,
                                        Integer.parseInt(availableSeat) - 1,
                                        dateToCome
                                    )
                                )
                        }
                        val list: List<SeatData?> = listOf(item.getValue(SeatData::class.java))
                        mutableLiveData.setValue(list)
                    }
                }
            }
        })
        return mutableLiveData
    }

    fun getReasonData(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
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
}