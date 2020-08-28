package com.example.adminmodule.Repository

import androidx.lifecycle.MutableLiveData
import com.example.adminmodule.Models.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DashboardRepo {

    fun showSeatDateWise(dateToCome: String): MutableLiveData<List<SeatData?>?> {
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
                        val list: List<SeatData?> = listOf(item.getValue(SeatData::class.java))
                        mutableLiveData.setValue(list)
                    }
                }
            }
        })
        return mutableLiveData
    }
}