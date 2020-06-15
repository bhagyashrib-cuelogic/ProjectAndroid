package com.cuelogic.seatbook

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Integer.parseInt

class requestUserData( context: Context, private var layoutResId:Int, var infoList:ArrayList<BookingData>) :
           ArrayAdapter<BookingData>(context,layoutResId,infoList){

    private lateinit var auth: FirebaseAuth

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view :View = layoutInflater.inflate(layoutResId,null)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val currentUser = user.uid!!
        val dataReference = FirebaseDatabase.getInstance().getReference("Booking")
        val textViewdate:TextView? = view.findViewById<TextView>(R.id.datefirebase)
        val textViewcheckintime: TextView? = view.findViewById<TextView>(R.id.checkintime)
        val textViewcheckouttime: TextView? = view.findViewById<TextView>(R.id.checkouttime)
        val textStatus: TextView? = view.findViewById<TextView>(R.id.status)
        val buttonCancel = view.findViewById<Button>(R.id.cancel)!!

        val info = infoList[position]
        textViewdate?.text=info.date
        textViewcheckintime?.text = info.CheckInTime
        textViewcheckouttime?.text = info.CheckOutTime
        textStatus?.text = info.status

        buttonCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("AlertDialog")
            builder.setMessage("Confirm cancel booking")
            builder.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                dataReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(item in snapshot.children){
                            val userUid = item.child("id").value.toString()
                            val bookedDate = item.child("date").value.toString()
                            val isEmpty = parseInt(item.child("booked").value.toString())

                            if(userUid==currentUser && bookedDate==info.date && isEmpty==0) {
                                val checkInTime = item.child("checkInTime").value.toString()
                                val checkOutTime = item.child("checkInTime").value.toString()
                                val reason = item.child("reason").value.toString()

                                dataReference.child(item.key.toString()).setValue(
                                    BookingData(
                                        userUid, bookedDate, checkInTime, checkOutTime, reason, "cancel", 1
                                    )
                                ).addOnCompleteListener(){
                                    Toast.makeText(context,"cancel booking",Toast.LENGTH_SHORT).show()
                                    updateSeatDataOnCancel(bookedDate)
                                    infoList.removeAt(position)
                                    notifyDataSetChanged();
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

        firebaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot : DataSnapshot) {
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

