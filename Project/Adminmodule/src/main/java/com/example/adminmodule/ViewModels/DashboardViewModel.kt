package com.example.adminmodule.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.Repository.DashboardRepo

class DashboardViewModel : ViewModel() {

    private var dashboardRepo = DashboardRepo()

    fun getSeatsDateWise(date: String): MutableLiveData<List<SeatData?>?> {
        return dashboardRepo.showSeatDateWise(date)
    }
}