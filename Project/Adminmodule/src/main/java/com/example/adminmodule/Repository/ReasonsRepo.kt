package com.example.adminmodule.Repository

import android.widget.ArrayAdapter
import com.google.firebase.database.*
import java.util.ArrayList

class ReasonsRepo {
    private lateinit var reasonReference: DatabaseReference

    fun getReasonData(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        reasonReference = FirebaseDatabase.getInstance().getReference("ReasonTable")
        reasonReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        spinnerDataList.add(item.value.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}