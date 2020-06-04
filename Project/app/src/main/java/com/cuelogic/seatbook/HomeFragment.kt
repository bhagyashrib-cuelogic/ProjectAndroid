package com.cuelogic.seatbook

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt
import java.lang.Integer.valueOf
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner
    private lateinit var listener: ValueEventListener
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var spinnerDataList: ArrayList<String>
    private lateinit var reasonReference: DatabaseReference
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        var context = container?.context!!
        spinner = view.findViewById(R.id.spinner)
        spinnerDataList = ArrayList<String>()
        adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerDataList
        )
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")

        //Spinner
        spinner.adapter = adapter
        getReasonData()


        //DatePicker
        var date = view?.findViewById<EditText>(R.id.edit_date)!!
        date.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var date = cal.get(Calendar.DATE)

            var dialog = DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, date)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000 - 1000 - 1000 - 1000
            dialog.show()
        }

        mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val month = month + 1
            var dateSet: String = "$day-$month-$year"
            Log.i("date", "$date")
            date.setText(dateSet)
            val dateToCome = date.toString()
            var flag=1
            showSeatDateWise(dateToCome,flag)
        }

        auth = FirebaseAuth.getInstance()
        val saveBtn = view.findViewById<Button>(R.id.button_save)!!
        saveBtn.setOnClickListener {
            saveDate()
        }
        return view
    }


    private fun saveDate() {
        val date = view?.findViewById<EditText>(R.id.edit_date)!!
        val checkInTime = view?.findViewById<EditText>(R.id.edit_checkInTime)!!
        val checkOutTime = view?.findViewById<EditText>(R.id.edit_checkOutTime)!!
        val reason = view?.findViewById<Spinner>(R.id.spinner)!!


        val dateToCome = date.text.toString()
        val checkTime = checkInTime.text.toString()
        val checkOut = checkOutTime.text.toString()
        val reasonDescription = reason.toString()


        if (dateToCome.isNotEmpty() && reasonDescription.isNotEmpty()) {
            val currentUser = auth.currentUser!!
            val currentUserUid = currentUser.uid
            val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
            val uidKey = firebaseReference.push().key!!

            firebaseReference.child(uidKey).setValue(
                BookingData(
                    currentUserUid,
                    dateToCome,
                    checkTime,
                    checkOut,
                    reasonDescription
                )
            ).addOnCompleteListener {
                Toast.makeText(activity, "Added successfully", Toast.LENGTH_LONG).show()
                var flag = 1
                showSeatDateWise(dateToCome,flag)
            }
        } else {
            date.error = "Please fill date"
            checkInTime.error = "Please fill check in time "
            checkOutTime.error = "Please fill check out time"
        }
        date.text.clear()
        checkInTime.text.clear()
        checkOutTime.text.clear()
    }

    private fun getReasonData() {
        listener = reasonReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        spinnerDataList.add(item.value.toString())
                    }
                    adapter.setNotifyOnChange(true)
                }
            }
        })
    }

    private fun showSeatDateWise(dateToCome: String,flag:Int) {
        var flag1 = flag
        var editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!

        var firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")
    //    firebaseReference.

                firebaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot : DataSnapshot) {
             Log.i("date","snapshot")
                    if (snapshot.exists()) {
                        Log.i("date","")
                        for (item in snapshot.children) {
                            val dateChoose = item.child("date")
                            val date = dateChoose.value.toString()

                                if (date == dateToCome) {

                                    val bookseat = item.child("bookedseat")
                                    var Bookedseat = bookseat.value.toString()

                                    val r = item.child("reservedseat")
                                    val r1 = r.value.toString()

                                    val totalS = item.child("totalseat")
                                    val totalseat = totalS.value.toString()

                                    if(flag1==1) {
                                      flag1=0
                                        var bookedseat = parseInt(Bookedseat) + 1
                                        Log.i("bookedseat", "$bookedseat")

                                        firebaseReference.child(item.key.toString()).setValue(SeatData(bookedseat,
                                            parseInt(totalseat), parseInt(r1), date))
                                        editTextBookedSeat.text = valueOf(bookedseat).toString()
                                    }else {
                                        editTextBookedSeat.text = valueOf(Bookedseat).toString()
                                    }
                                }
                        }
                    }
            }
            })
        }


}