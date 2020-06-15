package com.cuelogic.seatbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.model.BookingData

class UserBookingHistory(
    context: Context,
    private val layoutResId: Int,
    private val infoList: List<BookingData>
) :
    ArrayAdapter<BookingData>(context, layoutResId, infoList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewDate: TextView? = view.findViewById(R.id.datefirebase)
        val textViewCheckInTime: TextView? = view.findViewById(R.id.checkintime)
        val textViewCheckOutTime: TextView? = view.findViewById(R.id.checkouttime)
        val status: TextView? = view.findViewById(R.id.status)

        val info = infoList[position]
        textViewDate?.text = info.date
        textViewCheckInTime?.text = info.CheckInTime
        textViewCheckOutTime?.text = info.CheckOutTime
        status?.text = info.status
        return view

    }
}