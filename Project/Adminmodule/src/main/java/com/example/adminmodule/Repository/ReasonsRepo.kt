package com.example.adminmodule.Repository

import android.widget.ArrayAdapter
import com.google.firebase.database.*
import java.util.*


class ReasonsRepo {
    private lateinit var reasonReference: DatabaseReference

    var itemCount = 0

    fun getReasonData(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        spinnerDataList.add(item.value.toString())
                    }
                    itemCount = snapshot.children.count()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun addReason(reason: String) {
        //var totalReasons = itemCount + 1
        if (reason.isNotEmpty()) {
            val uidKey = reasonReference.push().key!!
            reasonReference.child(uidKey).setValue(
                reason
            )
        }
    }

    fun deleteReason(reason: String) {
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        if (item.value.toString() == reason) {
                            var key = (item.key).toString()
                            reasonReference.child(key).removeValue()

                        }

                    }
                }
            }
        })
    }


}