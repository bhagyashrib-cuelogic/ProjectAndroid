package com.cuelogic.seatbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cuelogic.seatbook.model.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


        employeeName = findViewById(R.id.name)
        saveButton = findViewById(R.id.saveButton)
        auth = FirebaseAuth.getInstance()
        cueId = findViewById(R.id.Cuid)
        designation = findViewById(R.id.designation)
        email = findViewById(R.id.email)

        employeeName.isEnabled = false
        cueId.isEnabled = false
        designation.isEnabled = false
        email.isEnabled = false
        showProfileInfo()

        saveButton.tag = 1
        saveButton.setOnClickListener { v ->
            if (v.tag == 1) {
                employeeName.isEnabled = true
                cueId.isEnabled = true
                designation.isEnabled = true
                v.tag = 0
                saveButton.text = "Save"
            } else {
                saveButton.text = "Edit"
                employeeName.isEnabled = false
                cueId.isEnabled = false
                designation.isEnabled = false
                v.tag = 1
                editProfileInfo()
            }
        }
    }

    private fun showProfileInfo() {
        val currentUserUid = auth.currentUser!!.uid
        val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
            .equalTo(currentUserUid)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        cueId = findViewById(R.id.Cuid)
        designation = findViewById(R.id.designation)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val empName = item.child("empName").value.toString().trim()
                    val empEmail = item.child("emailAddress").value.toString().trim()
                    val empCue = item.child("employeeProfile").value.toString()
                    val empDesignation = item.child("employeeDesignation").value.toString()
                    val isActivity = parseInt(item.child("active").value.toString())

                    if (isActivity == 0) {
                        name.setText(empName)
                        email.setText(empEmail)
                    } else {
                        name.setText(empName)
                        email.setText(empEmail)
                        cueId.setText(empCue)
                        designation.setText(empDesignation)
                    }
                }
            }
        })
    }

    private fun editProfileInfo() {
        val currentUserUid = auth.currentUser!!.uid
        val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
            .equalTo(currentUserUid)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        cueId = findViewById(R.id.Cuid)
        designation = findViewById(R.id.designation)


        var empName = name.text.toString()
        var empCueId = cueId.text.toString()
        var empDesignation = designation.text.toString()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val empEmail = item.child("emailAddress").value.toString()
                    val uid = item.child("uid").value.toString()

                    query.ref.child(item.key.toString()).setValue(
                        EmployeeData(
                            uid, empName,
                            empEmail, empDesignation, empCueId, "1"
                        )
                    ).addOnCompleteListener() {
                        Toast.makeText(
                            applicationContext,
                            "Updates Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        name.setText(empName)
                        email.setText(empEmail)
                        cueId.setText(empCueId)
                        designation.setText(empDesignation)
                        employeeName.isEnabled = false
                        cueId.isEnabled = false
                        designation.isEnabled = false
                        email.isEnabled = false
                    }
                }
            }
        })
    }
}
