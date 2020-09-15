package com.example.adminmodule.Repository

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.Utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConfigRepo {

    private lateinit var auth: FirebaseAuth
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")

    fun addTotalSeats(
        activity: FragmentActivity,
        dateToCome: String,
        seats: Int,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        auth = FirebaseAuth.getInstance()

        if (dateToCome.isNotEmpty()) {
            firebaseReference.child(dateToCome).setValue(
                SeatData(
                    0,
                    seats,
                    seats,
                    dateToCome
                )
            ).addOnCompleteListener {
                Utils.showToast("Seats Added successfully", activity)
                iAddonCompleteListener.addOnCompleteListener()
            }
        }
    }


    fun addSeatsInExistingSeats(
        activity: FragmentActivity,
        dateToCome: String,
        seats: Int,
        iAddonCompleteListener: IAddonCompleteListener
    ) {

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
                                    Integer.parseInt(bookedSeat),
                                    totalSeats.toInt() + seats,
                                    Integer.parseInt(availableSeat) + seats,
                                    dateToCome
                                )
                            )

                    }
                } else {
                    addTotalSeats(activity, dateToCome, seats, iAddonCompleteListener)
                }
            }
        })
    }
}