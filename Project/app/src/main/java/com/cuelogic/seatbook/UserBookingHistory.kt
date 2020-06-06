package com.cuelogic.seatbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserBookingHistory(private val contex: Context, private val layoutResId:Int, private val infoList:List<BookingData>) :
    ArrayAdapter<BookingData>(contex,layoutResId,infoList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewdate: TextView? = view.findViewById<TextView>(R.id.datefirebase)
        val textViewcheckintime: TextView? = view.findViewById<TextView>(R.id.checkintime)
        val textViewcheckouttime: TextView? = view.findViewById<TextView>(R.id.checkouttime)

        val info = infoList[position]
        textViewdate?.text = info.date
        textViewcheckintime?.text = info.CheckInTime
        textViewcheckouttime?.text = info.CheckOutTime

        return view

    }
}