package com.cuelogic.seatbook

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Integer.parseInt
import kotlin.contracts.contract

class requestUserData(private val contex: Context, private val layoutResId:Int, private val infoList:List<BookingData>) :
           ArrayAdapter<BookingData>(contex,layoutResId,infoList){

        private lateinit var auth: FirebaseAuth

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view :View = layoutInflater.inflate(layoutResId,null)
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser!!
        var currentUser = user.uid!!
        var dataReference = FirebaseDatabase.getInstance().getReference("Booking")

        val textViewdate:TextView? = view.findViewById<TextView>(R.id.datefirebase)
        val textViewcheckintime: TextView? = view.findViewById<TextView>(R.id.checkintime)
        val textViewcheckouttime: TextView? = view.findViewById<TextView>(R.id.checkouttime)

        val info = infoList[position]
        textViewdate?.text=info.date
        textViewcheckintime?.text = info.CheckInTime
        textViewcheckouttime?.text = info.CheckOutTime


        var btnCancel = view.findViewById<Button>(R.id.cancel)!!
        btnCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("AlertDialog")
            builder.setMessage("Confirm cancel booking")
            builder.setPositiveButton("Continue") { dialogInterface: DialogInterface, i: Int ->
                dataReference.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(item in snapshot.children){
                            var uid = item.child("id")
                            var userUid = uid.value.toString()
                            var userDate = item.child("date")
                            var bookedDate = userDate.value.toString()

                            if(userUid==currentUser && bookedDate==info.date) {
                                var Booked = item.child("booked")
                                var isEmpty = Booked.value.toString()
                                var falg = parseInt(isEmpty)
                                falg = 1

                                Log.i("hhh", "$isEmpty")

                                var inTime = item.child("checkInTime")
                                var checkInTime = inTime.value.toString()

                                var outTime = item.child("checkInTime")
                                var checkOutTime = outTime.value.toString()

                                var rea = item.child("reason")
                                var reason = rea.value.toString()

                                var bookStatus = item.child("status")
                                var status = bookStatus.value.toString()
                                status = "Cancel"

                                dataReference.child(item.key.toString()).setValue(
                                    BookingData(
                                        userUid, bookedDate,
                                        checkInTime, checkOutTime, reason, status, falg
                                    )
                                )
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



    private fun cancelBooking(){


    }

}