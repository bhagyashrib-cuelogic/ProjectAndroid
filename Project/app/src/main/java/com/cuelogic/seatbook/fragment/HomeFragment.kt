package com.cuelogic.seatbook.fragment

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
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.firebaseManager.HomeFirebaseData
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.SeatData
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
    private lateinit var homeFragment: FrameLayout
    private var homeFirebaseData = HomeFirebaseData()

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeFragment = view.findViewById(R.id.homeFragmentLayout)



        val context = container?.context!!
        val editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvailableSeat = view.findViewById<TextView>(R.id.text_reservedSeat)!!
        val buttonBookingDataSave = view.findViewById<Button>(R.id.button_save)!!
        val date = view.findViewById<TextView>(R.id.edit_date)!!
        val chooseTimeCheckIn = view.findViewById<TextView>(R.id.edit_checkInTime)
        val chooseTimeCheckOut = view.findViewById<TextView>(R.id.edit_checkOutTime)
        val dateIncrease = view.findViewById<ImageView>(R.id.forword_button)
        val dateDecrease = view.findViewById<ImageView>(R.id.backword_button)
        reasonSpinner = view.findViewById(R.id.spinner)


        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
        val timeFormat = SimpleDateFormat("HH:mm a")
        val currentTime = timeFormat.format(calendarInstance)
        chooseTimeCheckIn.text = currentTime.toString()
        chooseTimeCheckOut.text = currentTime.toString()
        homeFirebaseData.showSeatDateWise(currentDate, 0, editTextBookedSeat, editTextAvailableSeat)


        val calendar = Calendar.getInstance()
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateIncrease.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, +1)
            val tomorrow = dateFormat.format(calendar.time)
            date.text = tomorrow.toString()
            homeFirebaseData.showSeatDateWise(
                tomorrow.toString(),
                0,
                editTextBookedSeat,
                editTextAvailableSeat
            )
        }
        dateDecrease.setOnClickListener() {
            dateDecrease.isEnabled = true
            if (date.text.toString() != currentDate) {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                val tomorrow = dateFormat.format(calendar.time)
                date.text = tomorrow.toString()
                homeFirebaseData.showSeatDateWise(
                    tomorrow.toString(),
                    0,
                    editTextBookedSeat,
                    editTextAvailableSeat
                )
            } else {
                dateDecrease.isEnabled = false
            }
        }


        //DatePicker
        date.setOnTouchListener(OnTouchListener { v, event ->
            datePicker(context, date)
            false
        })

        //get Time
        chooseTimeCheckIn.setOnTouchListener { v, event ->
            selectTimePicker(chooseTimeCheckIn)
            false
        }

        chooseTimeCheckOut.setOnTouchListener { v, event ->
            selectTimePicker(chooseTimeCheckOut)
            false
        }

        auth = FirebaseAuth.getInstance()
        spinnerDataList = ArrayList<String>()
        spinnerDataList.add(0, "Select the Reason")
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reasonSpinner.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Spinner set adapter values
        adapter =
            ArrayAdapter<String>(context,
                R.layout.spinner_item, spinnerDataList)
        reasonSpinner.adapter = adapter

        getReasonData()

        //Onchange date show data
        date.text = currentDate
        date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val dateToCome = date.text.toString()
                homeFirebaseData.showSeatDateWise(dateToCome, 0, editTextBookedSeat, editTextAvailableSeat)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //Firebase get current user
        buttonBookingDataSave.setOnClickListener {
            val currentUserId = auth.currentUser!!.uid
            val dateToCome = date?.text.toString()
            val checkTime =chooseTimeCheckIn.text.toString()
            val checkOut = chooseTimeCheckOut.text.toString()
            val reasonDescription = reasonSpinner.selectedItem.toString()
            var isSelected :Boolean = true
            if (date.text.toString() == currentDate) {
                Toast.makeText(
                    activity,
                    "You can not book seat for today's date",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                homeFirebaseData.checkBookedSeatForParticularDateByUser(
                    activity!!,
                    currentUserId,
                    dateToCome,
                    checkTime,
                    checkOut,
                    reasonDescription,
                    editTextBookedSeat,
                    editTextAvailableSeat,
                    isSelected,date)
            }
        }
        return view
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

    //datePicker method.
    private fun datePicker(context: Context, date: TextView) {
        date.setOnClickListener {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val cal: Calendar = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val date = cal.get(Calendar.DATE)

            val dialog = DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,
                month,
                date
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }
        mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val month = month + 1
            var fm = "" + month
            var fd = "" + day
            if (month < 10) {
                fm = "0$month";
            }
            if (day < 10) {
                fd = "0$day";
            }
            val dateSet: String = "$fd-$fm-$year"
            date.text = dateSet
        }
    }




    private fun selectTimePicker(chooseTime: TextView) {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var mhour = hourOfDay
                val am_pm: String
                if (mhour < 12) {
                    am_pm = "am"
                } else {
                    am_pm = "pm"
                    mhour -= 12
                }
                chooseTime.text = String.format("$mhour:$minute $am_pm")
            },
            hour, minute, false
        )
        chooseTime.setOnClickListener {
            mTimePicker.show()
        }
    }
}

