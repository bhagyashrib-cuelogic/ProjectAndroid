package com.example.adminmodule.Repository

import android.annotation.SuppressLint
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.R
import com.example.adminmodule.UI.Adapters.BookedSeatsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BookedSeatsRepo {
    private lateinit var reasonReference: DatabaseReference
    var dataReference = FirebaseDatabase.getInstance().getReference("Booking")
    private lateinit var auth: FirebaseAuth
    var emoName = ""

    fun getUserNameList(userId: String): String {
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val userIdFromDb = item.child("uid").value.toString()
                        if (userId == userIdFromDb) {
                            emoName = item.child("empName").value.toString()
                        }

                    }


                }
            }
        })
        return emoName
    }

    @SuppressLint("SimpleDateFormat")
    fun showUserCurrentBookingList(
        userList: ArrayList<BookingModel>,
        listView: ListView,
        activity: FragmentActivity,
        iAddonCompleteListener: IAddonCompleteListener
    ) {

        auth = FirebaseAuth.getInstance()
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("d-MM-yyyy")
        var currentDate = dateFormat.format(calendarInstance)

        dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var adapter: BookedSeatsAdapter?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = Integer.parseInt(item.child("booked").value.toString())
                        var chooseDate = item.child("date").value.toString()
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val date1 = sdf.parse(chooseDate)
                        val date2 = sdf.parse(currentDate)
                        if (isBooked == 0) {
                            val infoUser = item.getValue(BookingModel::class.java)!!
                            userList.add(infoUser)
                            adapter = activity?.let {
                                BookedSeatsAdapter(
                                    it,
                                    R.layout.book_seat_item,
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
        })
    }
}