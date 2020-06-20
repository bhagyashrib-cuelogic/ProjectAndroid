package com.cuelogic.seatbook.repository.firebaseManager

import androidx.lifecycle.MutableLiveData
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.EmployeeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFirebaseData {

    private lateinit var auth: FirebaseAuth
    private val mutableLiveData: MutableLiveData<List<EmployeeData?>> = MutableLiveData<List<EmployeeData?>>()

    fun showProfileInfo():MutableLiveData<List<EmployeeData?>>{
        auth = FirebaseAuth.getInstance()
        val currentUserUid = auth.currentUser!!.uid
            val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
                .equalTo(currentUserUid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(snapshot: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                            val list:List<EmployeeData?> = listOf(item.getValue(EmployeeData::class.java))
                             mutableLiveData.setValue(list)
                    }
                }
            })
        return mutableLiveData
        }

    fun editProfileInfo(
        empName: String, empDesignation: String,
        empCueId: String, iAddonCompleteListener: IAddonCompleteListener
    ) {
        auth = FirebaseAuth.getInstance()
        val currentUserUid = auth.currentUser!!.uid
        val query = FirebaseDatabase.getInstance().getReference("Employees").orderByChild("uid")
            .equalTo(currentUserUid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val empEmail = item.child("emailAddress").value.toString()
                    val uid = item.child("uid").value.toString()
                    query.ref.child(item.key.toString()).setValue(
                        EmployeeData(uid, empName, empEmail, empDesignation, empCueId, "1")
                    )
                        .addOnCompleteListener {
                            iAddonCompleteListener.addOnCompleteListener()
                        }
                }
            }
        })
    }
}