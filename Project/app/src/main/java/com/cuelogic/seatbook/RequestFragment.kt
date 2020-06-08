package com.cuelogic.seatbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt

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


        dataReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var adapter: requestUserData?
                    for (item in snapshot.children) {
                        val userUid = item.child("id").value.toString()
                        val isBooked = parseInt(item.child("booked").value.toString())
                        if (userUid == currentUser && isBooked == 0) {
                            val infoUser = item.getValue(BookingData::class.java)!!
                            userList.add(infoUser)
                            Log.i("log","userList $userList")
                            Log.i("log","activityContext  $activity")
                            adapter = activity?.let { requestUserData(it, R.layout.user_request_list, userList) }
                            Log.i("log","userdata   $userList")
                            Log.i("log","context $context")
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
