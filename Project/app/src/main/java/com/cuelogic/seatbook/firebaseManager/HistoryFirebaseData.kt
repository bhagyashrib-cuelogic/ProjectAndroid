package com.cuelogic.seatbook.firebaseManager

import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.adapter.UserBookingHistory
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFirebaseData {

    private val dataReference = FirebaseDatabase.getInstance().getReference("Booking")

    fun showHistory(currentDate:String,currentUser:String,userList:MutableList<BookingData>,
                    context:FragmentActivity?,listView: ListView,iAddonCompleteListener: IAddonCompleteListener){

        dataReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    val userUid = item.child("id").value.toString()
                    val chooseDate = item.child("date").value.toString()
                    val isBooked = Integer.parseInt(item.child("booked").value.toString())

                    if(userUid==currentUser) {
                        if (chooseDate < currentDate || isBooked == 1) {
                            val infoUser = item.getValue(BookingData::class.java)!!
                            userList.add(infoUser)
                            val adapter: UserBookingHistory? =
                                UserBookingHistory(
                                    context!!,
                                    R.layout.history_list,
                                    userList
                                )
                            listView.adapter = adapter
                            adapter?.notifyDataSetChanged()
                            iAddonCompleteListener!!.addOnCompleteListener()
                        }
                    }
                }
            }
        })

    }
}