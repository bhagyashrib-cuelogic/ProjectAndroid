package com.cuelogic.seatbook.repository.firebaseManager

import android.annotation.SuppressLint
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.adapter.RequestUserData
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RequestFirebaseData {

    var dataReference = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var auth: FirebaseAuth

    fun showUserCurrentBookingList(userList:ArrayList<BookingData>,
                                   listView:ListView,activity:FragmentActivity,iAddonCompleteListener: IAddonCompleteListener){

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("d-MM-yyyy")
        var currentDate = dateFormat.format(calendarInstance)

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var adapter: RequestUserData?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = Integer.parseInt(item.child("booked").value.toString())
                        var chooseDate =item.child("date").value.toString()
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val date1 = sdf.parse(chooseDate)
                        val date2 = sdf.parse(currentDate)

                        if (userUid == currentUser) {
                            if (isBooked == 0 && date1.compareTo(date2)>=0) {
                                val infoUser = item.getValue(BookingData::class.java)!!
                                userList.add(infoUser)
                                adapter = activity?.let {
                                    RequestUserData(
                                        it,
                                        R.layout.user_request_list,
                                        userList
                                    )
                                }
                                listView.adapter = adapter!!
                                adapter.notifyDataSetChanged()
                                iAddonCompleteListener!!.addOnCompleteListener()
                            }
                        }
                    }
                }
            }
        })
    }
}