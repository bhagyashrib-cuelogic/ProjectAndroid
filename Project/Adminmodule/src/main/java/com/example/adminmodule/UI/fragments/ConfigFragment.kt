package com.example.adminmodule.UI.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.R
import com.example.adminmodule.UI.activities.ReasonActivity
import com.example.adminmodule.Utilities.Utils
import com.example.adminmodule.ViewModels.ConfigViewModel
import kotlinx.android.synthetic.main.add_seats_popup.*
import java.text.SimpleDateFormat
import java.util.*


class ConfigFragment : Fragment() {
    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var configViewModel: ConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)
        val context = container?.context!!
        val buttonConfigReason = view.findViewById<Button>(R.id.btnConfigReason)!!
        val buttonConfigSeats = view.findViewById<Button>(R.id.btnConfigSeats)!!

        configViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application())
            .create(ConfigViewModel::class.java)

        buttonConfigReason.setOnClickListener {
            var intent = Intent(activity, ReasonActivity::class.java)
            startActivity(intent)
        }

        buttonConfigSeats.setOnClickListener {
            showPopupAddSeats(context)
        }

        return view
    }


    private fun showPopupAddSeats(context: Context) {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_seats_popup, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        val totalSeats = mDialogView.findViewById<EditText>(R.id.etSeats)!!
        val seatDate = mDialogView.findViewById<TextView>(R.id.edit_date)!!
        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)

        val mAlertDialog = mBuilder.show()

        mAlertDialog.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

        seatDate.text = currentDate

        seatDate.setOnClickListener {
            datePicker(context, seatDate)
        }

        mAlertDialog.btnAdd.setOnClickListener {
            var seatsTextView = totalSeats.text
            if (seatsTextView.toString().isEmpty()) {
                Toast.makeText(
                    activity,
                    "Please Enter Number Of Seats",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val isConnected = Utils.isConnected(context)
                if (isConnected) {
                    var seats: Int = (seatsTextView.toString()).toInt()
                    var selectedDate = seatDate.text.toString()
                    addTotalSeats(seats, selectedDate)
                    mAlertDialog.dismiss()
                }

            }


        }

    }

    private fun addTotalSeats(seats: Int, date: String) {
        Log.d("Total seats", "$seats")
        Log.d("Selected date", date)
        configViewModel.addTotalSeats(date, seats, object : IAddonCompleteListener {
            override fun addOnCompleteListener() {
            }
        }, activity!!)
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