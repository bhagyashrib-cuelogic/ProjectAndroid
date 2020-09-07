package com.example.adminmodule.UI.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.R
import com.example.adminmodule.ViewModels.ReserveSeatViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class ReserveSeatActivity : AppCompatActivity() {

    private lateinit var backImage: ImageView
    private lateinit var reasonSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var reserveSeat: Button
    private lateinit var calenderLayout: RelativeLayout
    private lateinit var selectedDate: TextView
    private lateinit var tvCheckInTime: TextView
    private lateinit var tvCheckOutTime: TextView
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var reasonAdapter: ArrayAdapter<String>
    private lateinit var spinnerReasonList: ArrayList<String>
    private lateinit var userAdapter: ArrayAdapter<String>
    private lateinit var spinnerUserList: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    lateinit var reserveSeatsViewModel: ReserveSeatViewModel
    private lateinit var activityView: View
    private lateinit var currentDate: String
    private lateinit var currentTime: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_seats)

        backImage = findViewById(R.id.btnBack)
        reasonSpinner = findViewById(R.id.reason_spinner)
        userSpinner = findViewById(R.id.employee_spinner)
        reserveSeat = findViewById(R.id.button_reserveSeat)
        calenderLayout = findViewById(R.id.calender_layout)
        selectedDate = findViewById(R.id.tvDate)
        tvCheckInTime = findViewById(R.id.edit_checkInTime)
        tvCheckOutTime = findViewById(R.id.edit_checkOutTime)
        reserveSeatsViewModel = ViewModelProviders.of(this).get(ReserveSeatViewModel::class.java)

        activityView = View(applicationContext)

        backImage.setOnClickListener {
            this.finish()
        }

        setReasonData(reserveSeatsViewModel)
        setUsersData(reserveSeatsViewModel)

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        currentDate = dateFormat.format(calendarInstance)
        val timeFormat = SimpleDateFormat("HH:mm a")
        currentTime = timeFormat.format(calendarInstance)

        selectedDate.text = currentDate
        selectedDate.setOnTouchListener(View.OnTouchListener { v, event ->
            datePicker(this, selectedDate)
            false
        })

        tvCheckInTime.setOnTouchListener { v, event ->
            selectTimePicker(tvCheckInTime)
            false
        }

        tvCheckOutTime.setOnTouchListener { v, event ->
            selectTimePicker(tvCheckOutTime)
            false
        }

        reserveSeat.setOnClickListener {
            bookSeat(reserveSeatsViewModel)
        }


    }

    private fun bookSeat(reserveSeatsViewModel: ReserveSeatViewModel) {
        val reason = reasonSpinner.selectedItem.toString()
        val user = userSpinner.selectedItem.toString()
        val checkInTime = tvCheckInTime.text.toString()
        val checkOutTime = tvCheckOutTime.text.toString()
        val dateToCome = selectedDate?.text.toString()

        if (user == "" || user == "Select the User") {
            Toast.makeText(
                this,
                "Please select User",
                Toast.LENGTH_LONG
            ).show()
        } else if (reason == "" || reason == "Select the Reason") {
            Toast.makeText(
                this,
                "Please select Reason",
                Toast.LENGTH_LONG
            ).show()
        } else if (selectedDate.text.toString() == currentDate) {
            Toast.makeText(
                this,
                "You can not book seat for today's date",
                Toast.LENGTH_LONG
            ).show()
        } else {
            reserveSeatsViewModel.reserveSeat(
                this,
                dateToCome,
                checkInTime,
                checkOutTime,
                reason,
                user,
                object :
                    IAddonCompleteListener {
                    override fun addOnCompleteListener() {
                        tvCheckInTime.text = currentTime
                        tvCheckOutTime.text = currentTime
                    }
                })
        }
    }

    private fun setReasonData(reserveSeatsViewModel: ReserveSeatViewModel) {
        auth = FirebaseAuth.getInstance()
        spinnerReasonList = ArrayList<String>()
        spinnerReasonList.add(0, "Select the Reason")
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
        reasonAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, spinnerReasonList)
        reasonSpinner.adapter = reasonAdapter
        reserveSeatsViewModel.getReason(spinnerReasonList, reasonAdapter)
    }

    private fun setUsersData(reserveSeatsViewModel: ReserveSeatViewModel) {
        auth = FirebaseAuth.getInstance()
        spinnerUserList = ArrayList<String>()
        spinnerUserList.add(0, "Select the User")
        userSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userSpinner.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        userAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, spinnerUserList)
        userSpinner.adapter = userAdapter
        reserveSeatsViewModel.getUsers(spinnerUserList, userAdapter)
    }

    private fun selectTimePicker(chooseTime: TextView) {
        val timePicker: TimePickerDialog
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        timePicker = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var hour = hourOfDay
                val am_pm: String
                if (hour < 12) {
                    am_pm = "am"
                } else {
                    am_pm = "pm"
                    hour -= 12
                }
                chooseTime.text = String.format("$hour:$minute $am_pm")
            },
            hour, minute, false
        )
        chooseTime.setOnClickListener {
            timePicker.show()
        }
    }

    private fun datePicker(context: Context, date: TextView) {
        date.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val date = cal.get(Calendar.DATE)

            val dialog = DatePickerDialog(
                context,
                android.R.style.ThemeOverlay_Material_Dialog,
                mDateSetListener,
                year,
                month,
                date
            )
            cal.add(Calendar.DATE, 60)
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.datePicker.maxDate = cal.timeInMillis
            Toast.makeText(this, "You can book seat upto ${cal.time}", Toast.LENGTH_SHORT)
                .show()
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
}