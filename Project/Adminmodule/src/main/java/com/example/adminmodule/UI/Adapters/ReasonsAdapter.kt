package com.example.adminmodule.UI.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.adminmodule.R

class ReasonsAdapter(
    private val context: Activity, private val reason: ArrayList<String>
) :
    ArrayAdapter<String>(context, R.layout.reasons_item, reason) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.reasons_item, null, true)

        val titleText = rowView.findViewById(R.id.tvReason) as TextView
        val deleteReasons = rowView.findViewById<ImageView>(R.id.imgDelete)

        titleText.text = reason[position]

        return rowView
    }
}