package com.example.adminmodule.Repository

import android.app.Activity
import android.content.Context
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.Utilities.Utils
import com.google.firebase.database.*

class LoginRepo {

    private lateinit var reasonReference: DatabaseReference
    var isAdmin: Boolean = false

    fun     authenticateEmail(
        userEmail: String,
        activity: Context,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val empEmail = item.child("emailAddress").value.toString()
                        if (empEmail == userEmail) {
                            val designation = item.child("employeeDesignation").value.toString()
                            if (designation == "") {
                                isAdmin = false
                                Utils.showToast("You are not authorize to login", activity)
                                Utils.hideProgressDialog()
                                break
                            } else if (designation != "Admin") {
                                isAdmin = false
                                Utils.showToast("You are not authorize to login", activity)
                                Utils.hideProgressDialog()
                                break
                            } else {
                                isAdmin = true
                                iAddonCompleteListener.addOnCompleteListener()
                                break
                            }
                        }
                    }
                }
            }

        })
//        Log.e("LoginCall", "isAdmin: $isAdmin")
//        return isAdmin
    }
}