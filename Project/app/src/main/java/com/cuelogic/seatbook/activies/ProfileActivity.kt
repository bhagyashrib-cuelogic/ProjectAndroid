package com.cuelogic.seatbook.activies

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.ProfileViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.repository.firebaseManager.ProfileFirebaseData
import com.google.firebase.auth.FirebaseAuth
import java.lang.Integer.parseInt
import kotlin.collections.ArrayList


class ProfileActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var employeeName: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var cueId: EditText
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var reasonSpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        reasonSpinner = findViewById(R.id.spinner)
        var spinnerDataList: ArrayList<String> = arrayListOf("Intern", "Sr Soft Eng")
        spinnerDataList.add(0, "Select  Designation")
        reasonSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reasonSpinner.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        //Spinner set adapter values
        adapter =
            ArrayAdapter<String>(this, R.layout.spinner_designation, spinnerDataList)
        adapter.notifyDataSetChanged()
        reasonSpinner.adapter = adapter


        employeeName = findViewById(R.id.name)
        saveButton = findViewById(R.id.saveButton)
        auth = FirebaseAuth.getInstance()
        cueId = findViewById(R.id.Cuid)
        reasonSpinner = findViewById(R.id.spinner)
        email = findViewById(R.id.email)

        viewModel.showProfile()
            .observe(this,
                Observer<List<EmployeeData?>?> {
                    for (i in it!!) {
                        val active = parseInt(i?.isActive!!)
                        if (active == 0) {
                            employeeName.setText(i.empName)
                            email.setText(i.emailAddress)
                        } else {
                            employeeName.setText(i.empName)
                            email.setText(i.emailAddress)
                            cueId.setText(i.employeeProfile)
                          //  reasonSpinner.setTag(i.employeeDesignation)
                        }
                    }
                })

        employeeName.isEnabled = false
        cueId.isEnabled = false
        email.isEnabled = false
        saveButton.tag = 1
        saveButton.setOnClickListener { v ->
            if (v.tag == 1) {
                employeeName.isEnabled = true
                cueId.isEnabled = true
                v.tag = 0
                saveButton.text = "SAVE"
            } else {
                saveButton.text = "EDIT"
                employeeName.isEnabled = false
                cueId.isEnabled = false
                v.tag = 1
                val name = employeeName.text.toString()
                val cueIdEmp = cueId.text.toString()
                val designation = reasonSpinner.selectedItem.toString()
                viewModel.editProfile( name,designation, cueIdEmp, object : IAddonCompleteListener {
                        override fun addOnCompleteListener() {
                            Toast.makeText(applicationContext, "Updates Successfully", Toast.LENGTH_SHORT).show()
                            employeeName.isEnabled = false
                            cueId.isEnabled = false
                            email.isEnabled = false
                        }
                    })
            }
        }
    }

}
