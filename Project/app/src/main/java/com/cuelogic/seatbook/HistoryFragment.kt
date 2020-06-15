package com.cuelogic.seatbook

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.adapter.UserBookingHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {


    private lateinit var userList : MutableList<BookingData>
    private lateinit var listView : ListView
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val currentUser = user.uid!!

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
        Log.i("date","$currentDate")

        val dataReference = FirebaseDatabase.getInstance().getReference("Booking")
        listView = view.findViewById(R.id.listViewItem)
        userList = mutableListOf()
        val context = activity


        dataReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    val userUid = item.child("id").value.toString()
                    val chooseDate = item.child("date").value.toString()
                    val isBooked = parseInt(item.child("booked").value.toString())

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
                        }
                    }
                }
            }
        })
        return view
    }
}
