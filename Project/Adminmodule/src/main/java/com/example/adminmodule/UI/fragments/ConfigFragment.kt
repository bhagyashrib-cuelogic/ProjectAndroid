package com.example.adminmodule.UI.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.adminmodule.R
import com.example.adminmodule.UI.activities.ReasonActivity
import kotlinx.android.synthetic.main.add_seats_popup.*


class ConfigFragment : Fragment() {
    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)

        val buttonConfigReason = view.findViewById<Button>(R.id.btnConfigReason)!!
        val buttonConfigSeats = view.findViewById<Button>(R.id.btnConfigSeats)!!

        buttonConfigReason.setOnClickListener {
            var intent = Intent(activity, ReasonActivity::class.java)
            startActivity(intent)
        }

        buttonConfigSeats.setOnClickListener {
            showPopupAddSeats()
        }

        return view
    }


    private fun showPopupAddSeats() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_seats_popup, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)


        val mAlertDialog = mBuilder.show()

        mAlertDialog.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mAlertDialog.btnAdd.setOnClickListener {
            val seats = etSeats.text
            Log.d("Total seats", seats.toString())
        }

    }
}