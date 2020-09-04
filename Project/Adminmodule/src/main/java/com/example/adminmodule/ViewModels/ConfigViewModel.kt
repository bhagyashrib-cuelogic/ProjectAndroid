package com.example.adminmodule.ViewModels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.Repository.ConfigRepo
import com.example.adminmodule.Repository.DashboardRepo

class ConfigViewModel :ViewModel(){
    private var configRepo = ConfigRepo()

    fun addTotalSeats(date:String, seats:Int, iAddonCompleteListener: IAddonCompleteListener, activity: FragmentActivity) {
        return configRepo.addSeatsInExistingSeats(activity,date,seats,iAddonCompleteListener)
    }
}