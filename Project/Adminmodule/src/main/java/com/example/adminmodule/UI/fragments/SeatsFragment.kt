package com.example.adminmodule.UI.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cuelogic.seatbook.model.BookingData
import com.example.adminmodule.R
import com.example.adminmodule.UI.Adapters.BookedSeatsAdapter
import com.example.adminmodule.UI.Adapters.ReasonsAdapter
import com.example.adminmodule.UI.activities.ReasonActivity
import com.example.adminmodule.UI.activities.ReservSeatActivity

class SeatsFragment : Fragment() {

    private lateinit var bookingList: ArrayList<BookingData>
    private lateinit var bookingListView: ListView

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seats, container, false)

        bookingListView = view.findViewById(R.id.seatsListView)
        val addImage = view.findViewById<ImageView>(R.id.addIcon)!!
        bookingList = ArrayList()

        val seat1 = BookingData("Akshay Dhondge","1","1-9-2020","9-30 AM","6-30 PM","Meeting","Booked",0)
        val seat2 = BookingData("Pratik Ghadge","2","2-9-2020","9-30 AM","6-30 PM","Discussion","Booked",0)
        val seat3 = BookingData("Anand Lihare","3","3-9-2020","9-30 AM","6-30 PM","Hr Meet","Booked",0)

        bookingList.add(seat1)
        bookingList.add(seat2)
        bookingList.add(seat3)

        addImage.setOnClickListener {
            var intent = Intent(activity, ReservSeatActivity::class.java)
            startActivity(intent)
        }

        val reasonAdapter = BookedSeatsAdapter(activity!!,bookingList)
        bookingListView.adapter = reasonAdapter

        return view
    }
}