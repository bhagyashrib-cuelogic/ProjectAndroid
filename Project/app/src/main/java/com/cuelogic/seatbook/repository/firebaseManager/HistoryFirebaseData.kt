package com.cuelogic.seatbook.repository.firebaseManager

import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.HistoryViewModel
import com.cuelogic.seatbook.adapter.UserBookingHistory
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.Instant

class HistoryFirebaseData {

    private val dataReference = FirebaseDatabase.getInstance().getReference("Booking")

    fun showHistory(currentDate:String,currentUser:String,userList:MutableList<BookingData>,
                    context:FragmentActivity?,listView: ListView,iAddonCompleteListener: IAddonCompleteListener) {

        dataReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    val userUid = item.child("id").value.toString()
                    val chooseDate = item.child("date").value.toString()
                    val isBooked = Integer.parseInt(item.child("booked").value.toString())
                    val sdf = SimpleDateFormat("dd-MM-yyyy")
                    val date1 = sdf.parse(chooseDate)
                    val date2 = sdf.parse(currentDate)

                    if (userUid == currentUser) {
                        if (date1<date2 || isBooked == 1) {
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
                               iAddonCompleteListener.addOnCompleteListener()
                        }
                    }
                }
            }
        })
    }
}