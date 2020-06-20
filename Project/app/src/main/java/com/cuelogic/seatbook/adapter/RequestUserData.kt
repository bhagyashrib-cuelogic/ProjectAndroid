package com.cuelogic.seatbook.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.CancelBookingViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.repository.firebaseManager.FirebaseOperation
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Integer.parseInt

class RequestUserData(
    context: Context,
    private var layoutResId: Int,
    var infoList: ArrayList<BookingData>
) :
    ArrayAdapter<BookingData>(context, layoutResId, infoList) {

    private lateinit var auth: FirebaseAuth
    var firebaseReference = FirebaseOperation()
    var viewModel = CancelBookingViewModel()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(layoutResId, null)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val currentUser = user.uid!!
        val dataReference = FirebaseDatabase.getInstance().getReference("Booking")
        val textViewDate: TextView? = view.findViewById<TextView>(R.id.datefirebase)
        val textViewCheckInTime: TextView? = view.findViewById<TextView>(R.id.checkintime)
        val textViewCheckOutTime: TextView? = view.findViewById<TextView>(R.id.checkouttime)
        val textStatus: TextView? = view.findViewById<TextView>(R.id.status)
        val textReason: TextView? = view.findViewById<TextView>(R.id.textReason)
        val buttonCancel = view.findViewById<Button>(R.id.cancel)!!

        val info = infoList[position]
        textViewDate?.text = info.date
        textViewCheckInTime?.text = info.CheckInTime
        textViewCheckOutTime?.text = info.CheckOutTime
        textStatus?.text = info.status
        textReason?.text = info.Reason

        buttonCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Cancel Booking")
            builder.setMessage("Do you want cancel?")
            builder.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                viewModel.cancelBooking(info, object : IAddonCompleteListener {
                    override fun addOnCompleteListener() {
                        Toast.makeText(context, "cancel booking", Toast.LENGTH_SHORT)
                            .show()
                       viewModel.updateSeatAfterCancelBooking(info.date)
                        infoList.removeAt(position)
                        notifyDataSetChanged()
                    }
                })
            }
            builder.setNegativeButton("Cancel", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return view
    }
}

