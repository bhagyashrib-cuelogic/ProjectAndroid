package com.example.adminmodule.ViewModels

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.example.adminmodule.Repository.DashboardRepo
import com.example.adminmodule.Repository.ReasonsRepo

class ReasonsViewModel: ViewModel() {
    private var reasonsRepo = ReasonsRepo()

    fun getReason(spinnerDataList:ArrayList<String>,adapter: ArrayAdapter<String>){
        return reasonsRepo.getReasonData(spinnerDataList,adapter)
    }
}