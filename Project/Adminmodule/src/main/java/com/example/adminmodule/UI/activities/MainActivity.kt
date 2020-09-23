package com.example.adminmodule.UI.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.preferences.User
import com.example.adminmodule.R
import com.example.adminmodule.Utilities.Utils
import com.example.adminmodule.ViewModels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    private lateinit var userEmail: String
    private lateinit var etEmailEditText: TextInputEditText
    private lateinit var reasonReference: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this);
        Utils.intiProgressDialog(this)
        etEmailEditText = findViewById(R.id.etUsername)
        //etEmailEditText.setText("akshay.dhondge@cuelogic.com")

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
            Utils.showDialogBox("Please enter valid email id", this)

        } else if (!userEmail.contains("cuelogic.com")) {
            Utils.showDialogBox("Please enter official mail id",this)

        } else {
            Utils.showProgressDialog()
            applicationContext?.let {
                loginViewModel.doLogin()
                    .authenticateEmail(userEmail, it, object :
                        IAddonCompleteListener {
                        override fun addOnCompleteListener() {
                            val session = User(this@MainActivity)
                            session.setUId(true)
                            Utils.showToast("Login Successfully...", applicationContext)
                            Utils.hideProgressDialog()
                            val intent: Intent =
                                Intent(applicationContext, HomeScreenActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }

        }

    }

    private fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }





}
