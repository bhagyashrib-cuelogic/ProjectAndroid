package com.cuelogic.seatbook

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Integer.parseInt
import java.lang.Integer.valueOf
import java.text.SimpleDateFormat
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
    private lateinit var homeFragment:FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeFragment = view.findViewById(R.id.homeFragmentLayout)

        var context = container?.context!!
        spinner = view.findViewById(R.id.spinner)
        spinnerDataList = ArrayList<String>()
        adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerDataList
        )
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        var editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvaibleSeat=view?.findViewById<TextView>(R.id.text_reservedSeat)!!
      //  checkBookedSeatForParticularDateByUser()

        //Spinner
        spinner.adapter = adapter
        getReasonData()


        //Current date show data
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("d-M-yyyy")
        val currentDate = df.format(c)
        showSeatDateWise(currentDate,0,editTextBookedSeat,editTextAvaibleSeat)

        //DatePicker
        var date = view?.findViewById<EditText>(R.id.edit_date)!!
        date.setOnClickListener {
            datePicker(context,date)
        }
        //Onchange date show data
        date.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val dateToCome = date.text.toString()
                if(dateToCome==currentDate){
                    Snackbar.make(homeFragment,"You can not book seat for today's date",Snackbar.LENGTH_LONG).show()
                }
                var  flag=0
                    showSeatDateWise(dateToCome, flag,editTextBookedSeat,editTextAvaibleSeat)

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //Firebase get current user
        auth = FirebaseAuth.getInstance()
        val saveBtn = view.findViewById<Button>(R.id.button_save)!!
        saveBtn.setOnClickListener {
            val dateToCome = date.text.toString()
            if(dateToCome==currentDate) {
              Toast.makeText(activity,"You can not book seat for today's date",Toast.LENGTH_LONG).show()
           }else {
                saveDate()
            }
        }
        return view
    }


    private fun saveDate() {
        val date = view?.findViewById<EditText>(R.id.edit_date)!!
        val checkInTime = view?.findViewById<EditText>(R.id.edit_checkInTime)!!
        val checkOutTime = view?.findViewById<EditText>(R.id.edit_checkOutTime)!!
        val reason = view?.findViewById<Spinner>(R.id.spinner)!!
        val editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvaibleSeat=view?.findViewById<TextView>(R.id.text_reservedSeat)!!

        val dateToCome = date.text.toString()
        val checkTime = checkInTime.text.toString()
        val checkOut = checkOutTime.text.toString()
        val reasonDescription = reason.toString()
        val status:String= "Booked"
        val isBooked :Int =0


        if (dateToCome.isNotEmpty() && reasonDescription.isNotEmpty()) {
            val currentUser = auth.currentUser!!
            val currentUserUid = currentUser.uid
            val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
            val uidKey = firebaseReference.push().key!!

            firebaseReference.child(uidKey).setValue(BookingData(currentUserUid, dateToCome, checkTime, checkOut,reasonDescription,status,isBooked)
            ).addOnCompleteListener {
                Toast.makeText(activity, "Added successfully", Toast.LENGTH_LONG).show()
                var flag = 1
                showSeatDateWise(dateToCome,flag,editTextBookedSeat,editTextAvaibleSeat)
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

    //spinner date result
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

    //show seat data
    private fun showSeatDateWise(dateToCome: String,flag:Int,editTextBookedSeat:TextView,editTextAvailble:TextView) {
        var flag1 = flag

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

                                    val bookseat = item.child("booked")
                                    var Bookedseat = bookseat.value.toString()

                                    val availableSeat = item.child("available")
                                    var availablebook = availableSeat.value.toString()

                                    val totalS = item.child("total")
                                    val totalseat = totalS.value.toString()

                                    if(flag1==1) {
                                      flag1=0
                                        var bookedseat = parseInt(Bookedseat) + 1
                                        var available  = parseInt(availablebook)-1

                                        firebaseReference.child(item.key.toString()).setValue(SeatData(bookedseat,
                                            parseInt(totalseat),available, date))
                                        editTextBookedSeat.text = valueOf(bookedseat).toString()
                                        editTextAvailble.text = valueOf(available).toString()
                                        break
                                    }else {
                                        editTextBookedSeat.text = valueOf(Bookedseat).toString()
                                        editTextAvailble.text = valueOf(availablebook).toString()
                                        break
                                    }
                                }
                        }
                    }
            }
            })
        }

    //datepicker method
    fun datePicker(context: Context, date:EditText  ){
        date.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var date = cal.get(Calendar.DATE)

            var dialog = DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, date)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000 - 1000 - 1000 - 1000
            dialog.show()
        }
        mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val month =month+1
            var dateSet: String = "$day-$month-$year"
            Log.i("currentdate","$dateSet")
            date.setText(dateSet)
        }
    }

    fun checkBookedSeatForParticularDateByUser(){
          var firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
          auth = FirebaseAuth.getInstance()
          var currentUser = auth.currentUser!!
          var currentUserId = currentUser.uid
          val date = view?.findViewById<EditText>(R.id.edit_date)!!
          val dateToCome = date.text.toString()

          firebaseReference.addValueEventListener(object :ValueEventListener{
              override fun onCancelled(snapshot: DatabaseError) {
                  TODO("Not yet implemented")
              }

              override fun onDataChange(snapshot: DataSnapshot) {
                     for(item in snapshot.children){
                           val snapshotBookedDate = item.child("date")
                           val bookedDate = snapshotBookedDate.value.toString()

                           val snapshotBookedUSerId = item.child("id")
                           val bookedUserId = snapshotBookedUSerId.value.toString()

                           if(currentUserId==bookedUserId && dateToCome==bookedDate){
                                //Toast.makeText(activity,"You have booked seat for $dateToCome",Toast.LENGTH_LONG).show()
                           }
                           else{
                               saveDate()
                           }
                 }
              }
          })
    }
}

