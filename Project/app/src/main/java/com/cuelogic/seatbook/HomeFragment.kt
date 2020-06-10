package com.cuelogic.seatbook

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
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
    private lateinit var reasonSpinner: Spinner
    private lateinit var listener: ValueEventListener
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var spinnerDataList: ArrayList<String>
    private lateinit var reasonReference: DatabaseReference
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var homeFragment:FrameLayout

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeFragment = view.findViewById(R.id.homeFragmentLayout)

        val context = container?.context!!
        val editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvailableSeat= view.findViewById<TextView>(R.id.text_reservedSeat)!!
        val buttonBookingDataSave = view.findViewById<Button>(R.id.button_save)!!
        val date = view.findViewById<TextView>(R.id.edit_date)!!
        val chooseTimeCheckIn = view.findViewById<TextView>(R.id.edit_checkInTime)
        val chooseTimeCheckOut = view.findViewById<TextView>(R.id.edit_checkOutTime)


        //Current date show data
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
        showSeatDateWise(currentDate,0,editTextBookedSeat,editTextAvailableSeat,date)
        //DatePicker

       date.setOnTouchListener(OnTouchListener { v, event ->
        datePicker(context,date)
           false
       })


        //get Time
        chooseTimeCheckIn.setOnTouchListener { v, event->
            selectTimePicker(chooseTimeCheckIn)
            false
        }

        chooseTimeCheckOut.setOnTouchListener { v, event->
            selectTimePicker(chooseTimeCheckOut)
            false
        }



        auth = FirebaseAuth.getInstance()
        reasonSpinner = view.findViewById(R.id.spinner)
        spinnerDataList = ArrayList<String>()
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")

        reasonSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                reasonSpinner.setSelection(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Spinner set adapter values
        adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, spinnerDataList)
        reasonSpinner.adapter = adapter
        getReasonData()


        //Onchange date show data
        date.text = currentDate
        date.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val dateToCome = date.text.toString()
                if(dateToCome==currentDate){
                    Snackbar.make(homeFragment,"You can not book seat for today's date",Snackbar.LENGTH_LONG).show()
                }
                showSeatDateWise(dateToCome,0,editTextBookedSeat,editTextAvailableSeat,date)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //Firebase get current user
        buttonBookingDataSave.setOnClickListener {
            if(date.text.toString()==currentDate) {
              Toast.makeText(activity,"You can not book seat for today's date",Toast.LENGTH_LONG).show()
            }else {
               checkBookedSeatForParticularDateByUser()
            }
        }
        return view
    }


    private fun saveDate() {
        val date = view?.findViewById<TextView>(R.id.edit_date)!!
        val checkInTime = view?.findViewById<TextView>(R.id.edit_checkInTime)!!
        val checkOutTime = view?.findViewById<TextView>(R.id.edit_checkOutTime)!!
        val reason = view?.findViewById<Spinner>(R.id.spinner)!!
        val editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvailableSeat=view?.findViewById<TextView>(R.id.text_reservedSeat)!!

        val dateToCome = date?.text.toString()
        val checkTime = checkInTime.text.toString()
        val checkOut = checkOutTime.text.toString()
        val reasonDescription = reason.selectedItem.toString()

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)

        if (dateToCome.isNotEmpty() && reasonDescription.isNotEmpty()) {
            val currentUserUid = auth.currentUser!!.uid
            val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
            val uidKey = firebaseReference.push().key!!

            firebaseReference.child(uidKey).setValue(
                BookingData(currentUserUid, dateToCome, checkTime, checkOut,reasonDescription,"Booked",0)
            ).addOnCompleteListener {
                Toast.makeText(activity, "Added successfully", Toast.LENGTH_SHORT).show()
                showSeatDateWise(dateToCome,1,editTextBookedSeat,editTextAvailableSeat,date!!)
            }
        } else {
            checkInTime.error = "Please fill check in time "
            checkOutTime.error = "Please fill check out time"
        }

        checkInTime.text = "Check In"
        checkOutTime.text = "Check Out"
        date.text = currentDate

    }

    //Return reason.
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
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    //show seat data
    private fun showSeatDateWise(dateToCome: String,flag:Int,editTextBookedSeat:TextView,editTextAvailable:TextView,date:TextView) {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("SeatTable")
            .orderByChild("date")
            .equalTo(dateToCome)

        firebaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val bookedSeat = item.child("booked").value.toString()
                        val  availableSeat = item.child("available").value.toString()

                        if(flag==1) {
                            Toast.makeText(activity, "Show", Toast.LENGTH_SHORT).show()
                            firebaseReference.ref.child(item.key.toString())
                                .setValue(
                                    SeatData(
                                        parseInt(bookedSeat) + 1,
                                        200,
                                        parseInt(availableSeat)-1,
                                         dateToCome
                                    )
                                )
                        }
                        editTextBookedSeat.text = valueOf(bookedSeat).toString()
                        editTextAvailable.text = valueOf(availableSeat).toString()
                    }
                }
//                else
//                {
//                    Toast.makeText(activity, "You can not book for $dateToCome", Toast.LENGTH_SHORT).show()
//                    date.text.clear()
//                }
            }
        })
    }

    //datePicker method.
    private fun datePicker(context: Context, date:TextView ){
        date.setOnClickListener {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val cal: Calendar = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val date = cal.get(Calendar.DATE)

            val dialog = DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, date)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }
        mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val month =month+1
            var fm = "" + month
            var fd = "" + day
            if(month<10){
                fm = "0$month";
            }
            if (day<10){
                fd= "0$day";
            }
            val dateSet: String = "$fd-$fm-$year"
            date.text = dateSet
        }
    }

    //check seat already book or not.
    private fun checkBookedSeatForParticularDateByUser(){
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Booking")
        val currentUserId = auth.currentUser!!.uid
        val selectedDate = view?.findViewById<TextView>(R.id.edit_date)!!.text.toString()
        var isSelected  = true
        val date = view?.findViewById<TextView>(R.id.edit_date)!!

        firebaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    val bookedUserId = item.child("id").value.toString()
                    val bookedDate = item.child("date").value.toString()
                    val isDeleted = item.child("booked").value.toString()

                    if(selectedDate==bookedDate && bookedUserId==currentUserId && parseInt(isDeleted)==0){
                        Toast.makeText(activity,"You are already booked for this day",Toast.LENGTH_LONG).show()
                        isSelected=false
                        //date.text.clear()
                        break
                    }
                }
                if(isSelected) saveDate()
            }
        })
    }

    private fun selectTimePicker(chooseTime:TextView){
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> chooseTime.setText(String
                .format("%d : %d", hourOfDay, minute)) },
                hour, minute, false)

        chooseTime.setOnClickListener {
            mTimePicker.show()
        }
    }

    private fun getCurrentDate(){
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
        val date = view?.findViewById<TextView>(R.id.edit_date)!!
        date.text = currentDate
    }
}

