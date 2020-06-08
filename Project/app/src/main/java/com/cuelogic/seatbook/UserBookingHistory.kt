package com.cuelogic.seatbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserBookingHistory( context: Context, private val layoutResId:Int, private val infoList:List<BookingData>) :
    ArrayAdapter<BookingData>(context,layoutResId,infoList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewDate: TextView? = view.findViewById<TextView>(R.id.datefirebase)
        val textViewCheckInTime: TextView? = view.findViewById<TextView>(R.id.checkintime)
        val textViewCheckOutTime: TextView? = view.findViewById<TextView>(R.id.checkouttime)

        val info = infoList[position]
        textViewDate?.text = info.date
        textViewCheckInTime?.text = info.CheckInTime
        textViewCheckOutTime?.text = info.CheckOutTime

        return view

    }
}