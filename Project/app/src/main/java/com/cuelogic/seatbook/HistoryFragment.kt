package com.cuelogic.seatbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {


    private lateinit var userList : MutableList<BookingData>
    private lateinit var listView : ListView
    private lateinit var listShow : LayoutInflater
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser!!
        var currentUser = user.uid!!
        var dataReference = FirebaseDatabase.getInstance().getReference("Booking")
        listView = view.findViewById(R.id.listViewItem)
        userList = mutableListOf()


        dataReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    var uid = item.child("id")
                    var userUid = uid.value.toString()

                    if(userUid==currentUser){
                        var infoUser  = item.getValue(BookingData::class.java)!!
                        userList.add(infoUser)
                        val adapter:UserBookingHistory? = UserBookingHistory(context!!,R.layout.history_list,userList)
                        listView.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }
                }
            }


        })

        return view
    }

}
