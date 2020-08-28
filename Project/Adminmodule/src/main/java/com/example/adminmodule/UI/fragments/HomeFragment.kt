package com.example.adminmodule.UI.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adminmodule.R
import com.example.adminmodule.ViewModels.DashboardViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var homeFragment: FrameLayout
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeFragment = view.findViewById(R.id.homeFragmentLayout)

        dashboardViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application())
            .create(DashboardViewModel::class.java)

        val context = container?.context!!
        val date = view.findViewById<TextView>(R.id.edit_date)!!
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
        val nextDate = view.findViewById<ImageView>(R.id.idNextButton)
        val prevDate = view.findViewById<ImageView>(R.id.idPrevButton)
        val editTextBookedSeat = view?.findViewById<TextView>(R.id.text_bookedSeat)!!
        val editTextAvailableSeat = view.findViewById<TextView>(R.id.text_reservedSeat)!!
        date.text = currentDate

        getSeatData(editTextAvailableSeat, editTextBookedSeat, currentDate)

        date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val dateToCome = date.text.toString()
                getSeatData(editTextAvailableSeat, editTextBookedSeat, dateToCome)
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(activity!!);

        date.setOnTouchListener(View.OnTouchListener { v, event ->
            datePicker(context, date)
            false
        })

        val calendar = Calendar.getInstance()
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )


        nextDate.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, +1)
            val tomorrow = dateFormat.format(calendar.time)
            date.text = tomorrow.toString()

            getSeatData(editTextAvailableSeat, editTextBookedSeat, tomorrow.toString())



            nextDate.isEnabled = true
            val c = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            c.add(Calendar.DAY_OF_MONTH, +365)
            val next = dateFormat.format(c.time)
            val nextDay = dateFormat.parse(next)
            val tom = dateFormat.parse(tomorrow)
            if (nextDay == tom) {
                Toast.makeText(activity, "You can view seats up $tom", Toast.LENGTH_SHORT)
                    .show()
                nextDate.isEnabled = false
            }


        }
        prevDate.setOnClickListener {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = dateFormat.format(calendar.time)
            date.text = yesterday.toString()

            getSeatData(editTextAvailableSeat, editTextBookedSeat, yesterday.toString())
        }



        return view
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
            cal.add(Calendar.DATE, 180)
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

    private fun getSeatData(
        editTextAvailableSeat: TextView,
        editTextBookedSeat: TextView,
        currentDate: String
    ) {
        dashboardViewModel.getSeatsDateWise(currentDate).observe(activity!!, Observer {
            for (i in it!!) {
                if (i != null) {
                    editTextAvailableSeat.setText(Integer.valueOf(i.available).toString())
                    editTextBookedSeat.setText(Integer.valueOf(i.booked).toString())
                }
            }
        })
    }

}