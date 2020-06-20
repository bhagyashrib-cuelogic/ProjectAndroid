package com.cuelogic.seatbook.repository.firebaseManager

import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseOperation{

    private lateinit var auth: FirebaseAuth
    val dataReference = FirebaseDatabase.getInstance().getReference("Booking")

    fun cancelUserBookingSeat(info:BookingData,iAddonCompleteListener: IAddonCompleteListener) {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid!!

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val userUid = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isEmpty = Integer.parseInt(item.child("booked").value.toString())

                    if (userUid == currentUser && bookedDate == info.date && isEmpty == 0) {
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
                                "Cancelled",
                                1
                            )
                        )
                        iAddonCompleteListener.addOnCompleteListener()
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

            override fun onDataChange(snapshot : DataSnapshot) {
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

