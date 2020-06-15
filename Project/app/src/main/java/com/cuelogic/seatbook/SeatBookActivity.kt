package com.cuelogic.seatbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_seat_book.*


class SeatBookActivity : AppCompatActivity() {

    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_book)

        navController = Navigation.findNavController(this, R.id.fragment)
        //Setting the navigation controller to Bottom Nav
        bottomNav.setupWithNavController(navController)
        //Setting up the action bar
  //     NavigationUI.setupActionBarWithNavController(this, navController)

      //  var toolbar :androidx.appcompat.widget.Toolbar?= findViewById(R.id.customeToolbar)
        setSupportActionBar(customeToolbar)
        var profile = findViewById<ImageView>(R.id.profile_icon)
        profile.setOnClickListener{
            var intent = Intent(applicationContext,ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    //Setting Up the back button
//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, null)
//    }

}
