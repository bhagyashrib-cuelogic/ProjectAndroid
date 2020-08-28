package com.example.adminmodule.UI.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminmodule.R
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this);

        etUsername.setText("akshay.dhondge@cuelogic.com")
        etPassword.setText("123456")

        var adminLogin = findViewById<Button>(R.id.btnAdminLogin)
        adminLogin.setOnClickListener {
            login()
        }

    }

    private fun login() = if (etUsername.text.toString() != "akshay.dhondge@cuelogic.com") {
        Toast.makeText(applicationContext, "Please enter correct username", Toast.LENGTH_SHORT)
            .show()
    } else if (etPassword.text.toString() != "123456") {
        Toast.makeText(applicationContext, "Please enter correct password", Toast.LENGTH_SHORT)
            .show()
    } else {
        var intent: Intent = Intent(applicationContext, HomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}
