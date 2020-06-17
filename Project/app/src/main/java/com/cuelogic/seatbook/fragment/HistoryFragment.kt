package com.cuelogic.seatbook.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.adapter.UserBookingHistory
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.firebaseManager.HistoryFirebaseData
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
        val view = inflater.inflate(R.layout.fragment_history, container,false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val currentUser = user.uid!!

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)

        listView = view.findViewById(R.id.listViewItem)
        userList = mutableListOf()
        val context: FragmentActivity? = activity
        var firebaseHistory = HistoryFirebaseData()

        firebaseHistory.showHistory(currentDate,currentUser,userList,context,listView,  object :
            IAddonCompleteListener {
            override fun addOnCompleteListener() {
            }
        })
        return view
    }
}
