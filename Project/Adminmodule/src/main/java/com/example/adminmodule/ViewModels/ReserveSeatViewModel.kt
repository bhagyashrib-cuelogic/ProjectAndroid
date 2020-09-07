package com.example.adminmodule.ViewModels

import android.app.Activity
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.example.adminmodule.Repository.ReserveSeatRepo

class ReserveSeatViewModel : ViewModel() {
    private var reserveSeatRepo = ReserveSeatRepo()

    fun getReason(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        return reserveSeatRepo.getReasonData(spinnerDataList, adapter)
    }

    fun getUsers(spinnerDataList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        return reserveSeatRepo.getUserData(spinnerDataList, adapter)
    }

    fun reserveSeat(
        activity: Activity,selectedDate: String, checkTime: String, checkOut: String, reason: String, user: String,
        iAddonCompleteListener: IAddonCompleteListener
    ) {
        return reserveSeatRepo.getSelectedUserId(
            activity,
            selectedDate,
            checkTime,
            checkOut,
            reason,
            user,
            iAddonCompleteListener
        )
    }
}