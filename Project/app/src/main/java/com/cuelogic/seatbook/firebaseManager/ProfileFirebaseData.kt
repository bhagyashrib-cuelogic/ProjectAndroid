package com.cuelogic.seatbook.firebaseManager

import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFirebaseData {



    fun showProfileInfo(currentUserUid:String,name:EditText,email:EditText,cueId:EditText,designation:EditText) {

        val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
            .equalTo(currentUserUid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val empName = item.child("empName").value.toString().trim()
                    val empEmail = item.child("emailAddress").value.toString().trim()
                    val empCue = item.child("employeeProfile").value.toString()
                    val empDesignation = item.child("employeeDesignation").value.toString()
                    val isActivity = Integer.parseInt(item.child("active").value.toString())

                    if (isActivity == 0) {
                        name.setText(empName)
                        email.setText(empEmail)
                    } else {
                        name.setText(empName)
                        email.setText(empEmail)
                        cueId.setText(empCue)
                        designation.setText(empDesignation)
                    }
                }
            }
        })
    }


    fun editProfileInfo(currentUserUid: String,empName:String,empDesignation:String,empCueId:String,
                        employeeName:EditText,email:EditText,cueId:EditText,designation: EditText,
                        iAddonCompleteListener: IAddonCompleteListener) {
        val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
            .equalTo(currentUserUid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val empEmail = item.child("emailAddress").value.toString()
                    val uid = item.child("uid").value.toString()

                    query.ref.child(item.key.toString()).setValue(
                        EmployeeData(
                            uid, empName,
                            empEmail, empDesignation, empCueId, "1"
                        )
                    ).addOnCompleteListener(){
                        employeeName.setText(empName)
                        email.setText(empEmail)
                        cueId.setText(empCueId)
                        designation.setText(empDesignation)
                        iAddonCompleteListener!!.addOnCompleteListener()
                    }

                }

            }

        })
    }

}