package com.example.adminmodule.UI.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.adminmodule.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adminLogin = findViewById<Button>(R.id.btnAdminLogin)
        adminLogin.setOnClickListener {
            var intent: Intent = Intent(applicationContext, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
