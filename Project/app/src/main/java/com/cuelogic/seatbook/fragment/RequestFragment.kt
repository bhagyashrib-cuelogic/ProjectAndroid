package com.cuelogic.seatbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.adapter.RequestUserData
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.firebaseManager.RequestFirebaseData
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class RequestFragment : Fragment() {

    private lateinit var dataReference: DatabaseReference
    private lateinit var userList: ArrayList<BookingData>
    private lateinit var listView: ListView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request, container, false)
        userList = ArrayList()
        listView = view.findViewById(R.id.listViewItem)
        val firebaseRequest = RequestFirebaseData()

        activity?.let {
            firebaseRequest.showUserCurrentBookingList(userList,listView, it,object :
                IAddonCompleteListener {
                override fun addOnCompleteListener() {
                    Toast.makeText(activity,"Request", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }
}

