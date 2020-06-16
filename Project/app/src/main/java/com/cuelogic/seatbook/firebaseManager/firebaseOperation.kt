package com.cuelogic.seatbook.firebaseManager

import android.widget.Spinner
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class firebaseOperation {

    private lateinit var auth: FirebaseAuth
    private lateinit var reasonSpinner: Spinner
    private lateinit var reasonReference: DatabaseReference


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