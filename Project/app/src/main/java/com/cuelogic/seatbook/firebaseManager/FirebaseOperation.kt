package com.cuelogic.seatbook.firebaseManager

import android.widget.Toast
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseOperation  {


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var dataReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("Booking")

    fun cancelTicker(date: String, currentUser:String, iAddonCompleteListener: IAddonCompleteListener) {
        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val userUid = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isEmpty = Integer.parseInt(item.child("booked").value.toString())

                    if (userUid == currentUser && bookedDate == date && isEmpty == 0) {
                        val checkInTime = item.child("checkInTime").value.toString()
                        val checkOutTime = item.child("checkInTime").value.toString()
                        val reason = item.child("reason").value.toString()

                        dataReference.child(item.key.toString()).setValue(
                            BookingData(
                                userUid,
                                bookedDate,
                                checkInTime,
                                checkOutTime,
                                reason,
                                "cancel",
                                1
                            )
                        ).addOnCompleteListener() {
                            updateSeatDataOnCancel(bookedDate)
                            iAddonCompleteListener.addOnCompleteListener()
                        }
                    }
                }
            }
        })
    }

    private fun updateSeatDataOnCancel(dateToCome: String) {
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

                        firebaseReference.ref.child(item.key.toString())
                            .setValue(
                                SeatData(
                                    Integer.parseInt(bookedSeat) - 1,
                                    200,
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