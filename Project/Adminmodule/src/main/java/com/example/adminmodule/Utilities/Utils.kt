package com.example.adminmodule.Utilities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast


object Utils {
    private lateinit var nDialog: ProgressDialog

    fun isConnected(context: Context): Boolean {
        var connected = false
        try {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            var connected =
                nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message)
        }
        return connected
    }

    fun showToast(msg: String, context: Context) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_LONG
        ).show()

    }

    fun showDialogBox(msg: String, context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun intiProgressDialog(context: Context){
        nDialog = ProgressDialog(context)
    }

    fun showProgressDialog() {
        nDialog.setMessage("Please Wait...")
        nDialog.setTitle("Loading")
        nDialog.setCancelable(false)
        nDialog.show()

    }

    fun hideProgressDialog() {
        nDialog.dismiss()
    }
}