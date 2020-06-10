package com.cuelogic.seatbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RequestFragment : Fragment() {

    private lateinit var dataReference : DatabaseReference
    private lateinit var userList : MutableList<BookingData>
    private lateinit var listView : ListView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_request, container, false)

        dataReference = FirebaseDatabase.getInstance().getReference("Booking")
        userList = mutableListOf()
        listView = view.findViewById(R.id.listViewItem)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)



        dataReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var adapter: requestUserData?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = parseInt(item.child("booked").value.toString())
                        val chooseDate = item.child("date").value.toString()

                        if (userUid == currentUser) {
                            if (isBooked == 0 && chooseDate >= currentDate) {
                                val infoUser = item.getValue(BookingData::class.java)!!
                                userList.add(infoUser)
                                adapter = activity?.let { requestUserData(it, R.layout.user_request_list, userList)}
                                listView.adapter = adapter!!
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })
    return view
    }
}
