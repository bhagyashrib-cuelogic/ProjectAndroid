package com.cuelogic.seatbook

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
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
    private lateinit var listShow : LayoutInflater
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
        var user = auth.currentUser!!
        var currentUser = user.uid!!

        dataReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    var uid = item.child("id")
                    var userUid = uid.value.toString()

                    var isBooked = item.child("booked")
                    var isEmpty = isBooked.value.toString()
                    var falg = parseInt(isEmpty)

                    if(userUid==currentUser && falg==0){
                        val infoUser = item.getValue(BookingData::class.java)!!
                        userList.add(infoUser)
                        var adapter = requestUserData(context!!,R.layout.user_request_list,userList)
                        listView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
            return view
        }


    }
