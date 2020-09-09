package com.example.adminmodule.UI.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.R
import com.example.adminmodule.ViewModels.BookedSeatsViewModel

class BookedSeatsAdapter(
    private val context: Activity,
    private var layoutResId: Int,
    private val BookSeats: ArrayList<BookingModel>

) :
    ArrayAdapter<BookingModel>(context, layoutResId, BookSeats) {

    var viewModel = BookedSeatsViewModel()

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
        val cancelBooking = rowView.findViewById(R.id.btnCancelBooking) as Button

        empName.text = BookSeats[position].empName
        bookDate.text = BookSeats[position].date
        bookingStatus.text = BookSeats[position].status
        checkInTime.text = BookSeats[position].CheckInTime
        checkOutTime.text = BookSeats[position].CheckOutTime
        reason.text = BookSeats[position].Reason


        cancelBooking.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Cancel Booking")
            builder.setMessage("Are you sure you want to cancel?")
            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                viewModel.cancelBooking(BookSeats[position], object : IAddonCompleteListener {
                    override fun addOnCompleteListener() {
                        Toast.makeText(context, "Booking canceled successfully", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.updateSeatAfterCancelBooking(BookSeats[position].date)
                        BookSeats.removeAt(position)
                        notifyDataSetChanged()
                    }
                })
            }
            builder.setNegativeButton("No", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        return rowView
    }
}