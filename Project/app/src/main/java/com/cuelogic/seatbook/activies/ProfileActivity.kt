package com.cuelogic.seatbook.activies

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.ProfileViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import java.lang.Integer.parseInt


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

        val toolbar: Toolbar = findViewById(R.id.customeToolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        reasonSpinner = findViewById(R.id.spinner)
        val spinnerDataList: ArrayList<String> =
            arrayListOf("Intern", "Sr Soft Eng", "Jr Soft Eng", "Manager")
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
                                ArrayAdapter<String>(
                                    this,
                                    R.layout.spinner_designation,
                                    spinnerDataList
                                )
                            adapter.notifyDataSetChanged()
                            reasonSpinner.adapter = adapter

                        } else {
                            employeeName.setText(i.empName)
                            email.setText(i.emailAddress)
                            cueId.setText(i.employeeProfile)
                            spinnerDataList.add(0, "${i.employeeDesignation}")
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
                            adapter =
                                ArrayAdapter<String>(
                                    this,
                                    R.layout.spinner_designation,
                                    spinnerDataList
                                )
                            reasonSpinner.adapter = adapter
                        }
                    }
                })

        employeeName.isEnabled = false
        cueId.isEnabled = false
        email.isEnabled = false
        reasonSpinner.isEnabled = false
        reasonSpinner.isClickable = false
        saveButton.tag = 1

        saveButton.setOnClickListener { v ->
            if (v.tag == 1) {
                employeeName.isEnabled = true
                cueId.isEnabled = true
                reasonSpinner.isEnabled = true
                reasonSpinner.isClickable = true
                v.tag = 0
                saveButton.text = "SAVE"
            } else {
                saveButton.text = "EDIT"
                employeeName.isEnabled = false
                cueId.isEnabled = false
                reasonSpinner.isEnabled = false
                reasonSpinner.isClickable = false
                v.tag = 1
                val name = employeeName.text.toString()
                val cueIdEmp = cueId.text.toString()
                val designation = reasonSpinner.selectedItem.toString()
                viewModel.editProfile(name, designation, cueIdEmp, object : IAddonCompleteListener {
                    override fun addOnCompleteListener() {
                        Toast.makeText(
                            applicationContext,
                            "Updated Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        employeeName.isEnabled = false
                        cueId.isEnabled = false
                        email.isEnabled = false
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)

    }

}
