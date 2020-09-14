package com.example.adminmodule.UI.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.preferences.User
import com.example.adminmodule.R
import com.example.adminmodule.Utilities.Utils
import com.example.adminmodule.ViewModels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    private lateinit var userEmail: String
    private lateinit var etEmailEditText: TextInputEditText

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this);

        etEmailEditText = findViewById(R.id.etUsername)


        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        val adminLogin = findViewById<Button>(R.id.btnAdminLogin)
        adminLogin.setOnClickListener {
            val isConnected = Utils.isConnected(applicationContext)
            if (isConnected) {
                login()
            } else {
                Utils.showToast("No internet Connection", applicationContext)
            }

        }

    }

    private fun login() {
        userEmail = etEmailEditText.text.toString().trim()
        if (userEmail == "") {
            Utils.showDialogBox("Please enter email id", this)

        } else if (!validEmail(userEmail)) {
            Utils.showDialogBox("Please enter valid email id", applicationContext)

        } else if (!userEmail.contains("cuelogic.com")) {
            Utils.showDialogBox("Please enter official mail id",applicationContext)

        } else {
            val isAdmin = loginViewModel.authenticateEmail(userEmail, this)
            if (isAdmin) {
                val session =
                    User(this)
                session.setUId(true)
                Utils.showToast("Login Successfully...",applicationContext)
                val intent: Intent = Intent(applicationContext, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }

    private fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}
