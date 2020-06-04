package com.cuelogic.seatbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        NavigationUI.setupActionBarWithNavController(this, navController)

    }
    //Setting Up the back button
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}
