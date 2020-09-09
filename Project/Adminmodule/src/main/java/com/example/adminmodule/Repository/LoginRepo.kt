package com.example.adminmodule.Repository

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.*

class LoginRepo {

    private lateinit var reasonReference: DatabaseReference
    var isAdmin: Boolean = false

    fun authenticateEmail(userEmail: String, activity: Activity): Boolean {
        reasonReference = FirebaseDatabase.getInstance().getReference("Employees")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
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
                                Toast.makeText(
                                    activity,
                                    "You are not authorize to login",
                                    Toast.LENGTH_LONG
                                ).show()
                                break
                            } else if(designation != "Admin" ) {
                                isAdmin = false
                                Toast.makeText(
                                    activity,
                                    "You are not authorize to login",
                                    Toast.LENGTH_LONG
                                ).show()
                                break
                            }else{
                                isAdmin = true
                                break
                            }
                        }
                    }
                }
            }
        })
        return isAdmin
    }
}