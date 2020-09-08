package com.example.adminmodule.UI.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.R

class BookedSeatsAdapter(
    private val context: Activity,
    private var layoutResId: Int,
    val BookSeats: ArrayList<BookingModel>

) :
    ArrayAdapter<BookingModel>(context, layoutResId, BookSeats) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val rowView = layoutInflater.inflate(layoutResId,null)

        val empName = rowView.findViewById(R.id.tvName) as TextView
        val bookDate = rowView.findViewById(R.id.datefirebase) as TextView
        val bookingStatus = rowView.findViewById(R.id.bookingStatus) as TextView
        val checkInTime = rowView.findViewById(R.id.checkintime) as TextView
        val checkOutTime = rowView.findViewById(R.id.checkouttime) as TextView
        val reason = rowView.findViewById(R.id.textReason) as TextView


        empName.text = BookSeats[position].empName
        bookDate.text = BookSeats[position].date
        bookingStatus.text = BookSeats[position].status
        checkInTime.text = BookSeats[position].CheckInTime
        checkOutTime.text = BookSeats[position].CheckOutTime
        reason.text = BookSeats[position].Reason

        return rowView
    }
}