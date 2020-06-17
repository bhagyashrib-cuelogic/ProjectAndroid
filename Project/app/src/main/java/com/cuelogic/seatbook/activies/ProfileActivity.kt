package com.cuelogic.seatbook.activies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.firebaseManager.ProfileFirebaseData
import com.cuelogic.seatbook.model.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_seat_book.*
import java.lang.Integer.parseInt


class ProfileActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var employeeName: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var cueId: EditText
    private lateinit var designation: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(customeToolbar)

        employeeName = findViewById(R.id.name)
        saveButton = findViewById(R.id.saveButton)
        auth = FirebaseAuth.getInstance()
        cueId = findViewById(R.id.Cuid)
        designation = findViewById(R.id.designation)
        email = findViewById(R.id.email)
        val currentUserUid = auth.currentUser!!.uid
        val profileData = ProfileFirebaseData()
        profileData.showProfileInfo(currentUserUid,employeeName,email,cueId,designation)

        employeeName.isEnabled = false
        cueId.isEnabled = false
        designation.isEnabled = false
        email.isEnabled = false

        saveButton.tag = 1
        saveButton.setOnClickListener { v ->
            if (v.tag == 1) {
                employeeName.isEnabled = true
                cueId.isEnabled = true
                designation.isEnabled = true
                v.tag = 0
                saveButton.text = "Save"
            } else {
                saveButton.text = "Update"
                employeeName.isEnabled = false
                cueId.isEnabled = false
                designation.isEnabled = false
                v.tag = 1
                var name = employeeName.text.toString()
                var emailId = email.text.toString()
                var cueIdEmp = cueId.text.toString()
                var desig =designation.text.toString()
                profileData.editProfileInfo(currentUserUid,name,desig,cueIdEmp,
                    employeeName,email,cueId,designation,object : IAddonCompleteListener {
                        override fun addOnCompleteListener() {
                            Toast.makeText(
                                applicationContext,
                                "Updates Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            employeeName.isEnabled = false
                            cueId.isEnabled = false
                            designation.isEnabled = false
                            email.isEnabled = false
                        }


                    })
            }
        }
    }
}
