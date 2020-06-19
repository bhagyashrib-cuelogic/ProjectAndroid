package com.cuelogic.seatbook.repository.firebaseManager

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class HomeFirebaseData {


    private lateinit var auth: FirebaseAuth
    private val calendarInstance = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val currentDate: String = dateFormat.format(calendarInstance)
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")


     fun saveDate(checkTime: String,checkOut: String,reasonDescription: String,dateToCome: String,activity: FragmentActivity) {

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
            }
        }
//        checkInTime.text = currentTime.toString()
//        checkOutTime.text = currentTime.toString()
       // date.text = currentDate

    }
    fun checkBookedSeatForParticularDateByUser(
        activity: FragmentActivity,
        currentUserId: String,
        dateToCome: String,
        checkInTime: String,
        checkOutTime: String,
        reasonDescription: String,
        editTextBookedSeat: TextView,
        editTextAvailable: TextView,
        isSelected:Boolean,date:TextView
    ) {
        var isBooked = isSelected
        isBooked = true

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val bookedUserId = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isDeleted = item.child("booked").value.toString()

                    if (dateToCome == bookedDate && bookedUserId == currentUserId && Integer.parseInt(isDeleted) == 0) {
                        Toast.makeText(
                            activity,
                            "You are already booked for this day",
                            Toast.LENGTH_LONG
                        ).show()
                        isBooked = false
                        break
                    }
                }
                if (isBooked) saveDate(checkInTime,checkOutTime,reasonDescription,dateToCome,activity)
            }
        })
    }

    fun showSeatDateWise(dateToCome: String, flag: Int):MutableLiveData<List<SeatData?>?> {
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
                        val list:List<SeatData?> = listOf(item.getValue(SeatData::class.java))
                        mutableLiveData.setValue(list)

                    }
                }
            }
        })
        return mutableLiveData
    }
}