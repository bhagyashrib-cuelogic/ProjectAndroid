package com.example.adminmodule.UI.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.R
import com.example.adminmodule.UI.Adapters.ReasonsAdapter
import com.example.adminmodule.ViewModels.ReasonsViewModel
import kotlinx.android.synthetic.main.activity_reasons.*
import kotlinx.android.synthetic.main.add_reasons_popup.*
import kotlinx.android.synthetic.main.add_seats_popup.btnCancel


class ReasonActivity : AppCompatActivity() {
    private lateinit var reasonslist: ListView
    private lateinit var reasonDataList: ArrayList<String>
    private lateinit var reasonAdapter: ArrayAdapter<String>
    lateinit var reasonViewModel: ReasonsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reasons)

        reasonslist = findViewById(R.id.reasonsListView)
        reasonDataList = ArrayList()

        getReasonsListing()

        btnBack.setOnClickListener {
            this.finish()
        }


        val context: Context = this
        imgAdd.setOnClickListener {
            showPopupAddReasons(context)
        }

    }

    private fun getReasonsListing() {
        reasonDataList.clear()
        reasonViewModel = ViewModelProviders.of(this).get(ReasonsViewModel::class.java)
        reasonAdapter = ReasonsAdapter(this, reasonDataList)
        reasonslist.adapter = reasonAdapter
        reasonViewModel.getReason(reasonDataList, reasonAdapter)
    }

    private fun showPopupAddReasons(context: Context) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_reasons_popup)

        val reasons = dialog.findViewById<EditText>(R.id.etReasons)!!

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnAddReasons.setOnClickListener {
            var reason = reasons.text.toString()
            if (reason.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please Enter Reason",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.d("Entered Reason:", reason)
                addReasons(reason)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun addReasons(reason: String) {
        reasonViewModel.addNewReasons(reason)
        getReasonsListing()
        Toast.makeText(applicationContext, "Reason Added successfully", Toast.LENGTH_SHORT).show()
    }
}