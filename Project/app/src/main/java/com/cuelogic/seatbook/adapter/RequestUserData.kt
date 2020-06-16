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
import com.cuelogic.seatbook.firebaseManager.firebaseOperation
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.SeatData
import com.google.firebase.FirebaseOptions
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
    var firebaseReference = firebaseOperation()

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
        val buttonCancel = view.findViewById<Button>(R.id.cancel)!!

        val info = infoList[position]
        textViewDate?.text = info.date
        textViewCheckInTime?.text = info.CheckInTime
        textViewCheckOutTime?.text = info.CheckOutTime
        textStatus?.text = info.status

        buttonCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Cancel Booking")
            builder.setMessage("Do you want cancel?")
            builder.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                dataReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (item in snapshot.children) {
                            val userUid = item.child("id").value.toString()
                            val bookedDate = item.child("date").value.toString()
                            val isEmpty = parseInt(item.child("booked").value.toString())

                            if (userUid == currentUser && bookedDate == info.date && isEmpty == 0) {
                                val checkInTime = item.child("checkInTime").value.toString()
                                val checkOutTime = item.child("checkInTime").value.toString()
                                val reason = item.child("reason").value.toString()

                                dataReference.child(item.key.toString()).setValue(
                                    BookingData(
                                        userUid,
                                        bookedDate,
                                        checkInTime,
                                        checkOutTime,
                                        reason,
                                        "cancel",
                                        1
                                    )
                                ).addOnCompleteListener() {
                                    Toast.makeText(context, "cancel booking", Toast.LENGTH_SHORT)
                                        .show()
                                    firebaseReference.updateSeatDataOnCancel(bookedDate)
                                    infoList.removeAt(position)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                })
            }
            builder.setNegativeButton("Cancel", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return view
    }

    private fun updateSeatDataOnCancel(dateToCome: String) {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")
            .orderByChild("date")
            .equalTo(dateToCome)

        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val bookedSeat = item.child("booked").value.toString()
                        val availableSeat = item.child("available").value.toString()

                        firebaseReference.ref.child(item.key.toString())
                            .setValue(
                                SeatData(
                                    parseInt(bookedSeat) - 1,
                                    200,
                                    parseInt(availableSeat) + 1,
                                    dateToCome
                                )
                            )
                    }
                }
            }
        })
    }
}

