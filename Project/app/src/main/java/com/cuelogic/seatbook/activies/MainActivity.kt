package com.cuelogic.seatbook.activies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.preferences.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var RC_SIGN_IN: Int = 123
    private lateinit var auth: FirebaseAuth
    private lateinit var layout: RelativeLayout
    private var emailAddress: String? = null
    private var employeeName: String? = null
    private var employeeDesignation: String? = null
    private var employeeProfile: String? = null


    override fun onStart() {
        super.onStart()
        var session = User(MainActivity@ this)
        var value = session.getUId()
        Log.d("EmployeeName", "$value")
        if (value != "") {
//            var intent: Intent = Intent(applicationContext, SeatBookActivity::class.java)
//            startActivity(intent)
//            finish()
            var intent: Intent = Intent(applicationContext, SeatBookActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.relativeOnes)
        auth = FirebaseAuth.getInstance()
        //    var signIn = findViewById<Button>(R.id.button_googleSign)
        createSign()

        var signIn = findViewById<Button>(R.id.button_googleSign)
        signIn.setOnClickListener() {
            signIn()
        }
    }

    private fun createSign() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Snackbar.make(layout, "${account.email}", Snackbar.LENGTH_LONG).show()

                this.auth.fetchProvidersForEmail(account.email!!)
                    .addOnCompleteListener(this, OnCompleteListener() { task ->
                        if (task.isComplete) {
                            val check: Boolean = !task.result!!.providers!!.isEmpty()
                            if (!check) {
                                firebaseAuthWithGoogle(account.idToken!!)
                            } else {
                                firebaseWithSecondTime(account.idToken!!)
                            }
                        }
                    })
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Something went to wrong   $e.message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val session =
                        User(MainActivity@ this)
                    val user = auth.currentUser
                    val uid = user?.uid
                    //Store UID into sharedpreferences
                    session.setUId(uid.toString())

                    emailAddress = user?.email
                    employeeName = user?.displayName
                    employeeDesignation = ""
                    employeeProfile = ""

                    val firebaseReference =
                        FirebaseDatabase.getInstance().getReference("Employees")
                    val uidKey = firebaseReference.push().key

                    firebaseReference.child(uidKey!!)
                        .setValue(
                            EmployeeData(
                                uid!!,
                                employeeName!!,
                                emailAddress!!,
                                employeeDesignation,
                                employeeProfile,
                                "0"
                            )
                        ).addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "successfully login",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    val intent = Intent(applicationContext, SeatBookActivity::class.java)
                    startActivity(intent)
                }
            }
    }

    private fun firebaseWithSecondTime(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val session =
                        User(MainActivity@ this)
                    val user = auth.currentUser
                    val uid = user?.uid
                    //Store UID into sharedpreferences
                    session.setUId(uid.toString())
                    val intent: Intent = Intent(applicationContext, SeatBookActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}
