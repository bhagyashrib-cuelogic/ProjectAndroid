package com.cuelogic.seatbook.activies

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.cuelogic.seatbook.R
import kotlinx.android.synthetic.main.activity_seat_book.*

class SeatBookActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_book)

        navController = Navigation.findNavController(this,
            R.id.fragment
        )
        //Setting the navigation controller to Bottom Nav
        bottomNav.setupWithNavController(navController)

        setSupportActionBar(customeToolbar)
        var profile = findViewById<ImageView>(R.id.profile_icon)
        profile.setOnClickListener {
            var intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

}
