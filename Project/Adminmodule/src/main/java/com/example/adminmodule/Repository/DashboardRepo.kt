package com.example.adminmodule.Repository

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.adminmodule.Models.SeatData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardRepo {

    fun showSeatDateWise(
        dateToCome: String,
        activity: FragmentActivity
    ): MutableLiveData<List<SeatData?>?> {
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
                        mutableLiveData.value = list
                    }
                } else {
                    mutableLiveData.value = emptyList()

                }
            }
        })
        return mutableLiveData
    }
}