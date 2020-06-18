package com.cuelogic.seatbook.activies

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.ProfileViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.repository.firebaseManager.ProfileFirebaseData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_seat_book.*
import java.lang.Integer.parseInt


class ProfileActivity : AppCompatActivity(){

    private lateinit var saveButton: Button
    private lateinit var employeeName: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var cueId: EditText
    private lateinit var designation: EditText
    private lateinit var  viewModel : ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(customeToolbar)
       viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        employeeName = findViewById(R.id.name)
        saveButton = findViewById(R.id.saveButton)
        auth = FirebaseAuth.getInstance()
        cueId = findViewById(R.id.Cuid)
        designation = findViewById(R.id.designation)
        email = findViewById(R.id.email)
        val currentUserUid = auth.currentUser!!.uid


        viewModel.showProfile()
            .observe(this,
                Observer<List<EmployeeData?>?> {
                    for( i in it!!) {
                        val active = parseInt(i?.isActive!!)
                        if(active==0)
                        {
                            employeeName.setText(i.empName)
                            email.setText(i.emailAddress)
                        }else {
                            employeeName.setText(i.empName)
                            email.setText(i.emailAddress)
                            cueId.setText(i.emailAddress)
                            designation.setText(i.employeeDesignation)
                        }
                    }
                })

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
