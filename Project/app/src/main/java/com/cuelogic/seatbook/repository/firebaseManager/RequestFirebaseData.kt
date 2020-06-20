package com.cuelogic.seatbook.repository.firebaseManager

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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RequestFirebaseData {


    var dataReference = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var auth: FirebaseAuth



    fun showUserCurrentBookingList(userList:ArrayList<BookingData>,
                                   listView:ListView,activity:FragmentActivity,iAddonCompleteListener: IAddonCompleteListener){

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var adapter: RequestUserData?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = Integer.parseInt(item.child("booked").value.toString())
                        val chooseDate = item.child("date").value.toString()

                        if (userUid == currentUser) {
                            if (isBooked == 0 && chooseDate >= currentDate) {
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