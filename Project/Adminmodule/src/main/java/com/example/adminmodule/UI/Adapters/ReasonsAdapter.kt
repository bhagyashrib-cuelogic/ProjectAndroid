package com.example.adminmodule.UI.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.adminmodule.R
import com.example.adminmodule.ViewModels.ReasonsViewModel

class ReasonsAdapter(
    private val context: Activity, private val reason: ArrayList<String>
) :
    ArrayAdapter<String>(context, R.layout.reasons_item, reason) {

    lateinit var reasonsViewModel : ReasonsViewModel

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.reasons_item, null, true)

        reasonsViewModel = ReasonsViewModel()

        val titleText = rowView.findViewById(R.id.tvReason) as TextView
        val deleteReasons = rowView.findViewById<ImageView>(R.id.imgDelete)

        titleText.text = reason[position]
        deleteReasons.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Reason")
            builder.setMessage("Are you sure you want delete this reason?")
            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                reasonsViewModel.deleteReason(reason[position])
                reason.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, "Reason Deleted successfully", Toast.LENGTH_SHORT)
                    .show()

            }
            builder.setNegativeButton("No", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        return rowView
    }
}