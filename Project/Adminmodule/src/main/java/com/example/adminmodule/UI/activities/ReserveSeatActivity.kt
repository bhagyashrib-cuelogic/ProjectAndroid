package com.example.adminmodule.UI.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.adminmodule.R

class ReserveSeatActivity : AppCompatActivity() {

    private lateinit var backImage: ImageView
    private lateinit var reasonSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var reserveSeat: Button
    private lateinit var calenderLayout: RelativeLayout
    private lateinit var selectedDate: TextView
    private lateinit var checkInTime: TextView
    private lateinit var checkOutTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_seats)

        backImage = findViewById(R.id.btnBack)
        reasonSpinner = findViewById(R.id.reason_spinner)
        userSpinner = findViewById(R.id.employee_spinner)
        reserveSeat = findViewById(R.id.button_reserveSeat)
        calenderLayout = findViewById(R.id.calender_layout)
        selectedDate = findViewById(R.id.tvDate)
        checkInTime = findViewById(R.id.edit_checkInTime)
        checkOutTime = findViewById(R.id.edit_checkOutTime)

    }

}