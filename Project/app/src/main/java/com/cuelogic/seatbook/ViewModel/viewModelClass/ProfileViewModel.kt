package com.cuelogic.seatbook.ViewModel.viewModelClass

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.repository.firebaseManager.ProfileFirebaseData


class ProfileViewModel: ViewModel() {

    private var profile = ProfileFirebaseData()

    fun showProfile() : MutableLiveData<List<EmployeeData?>> {
        return profile.showProfileInfo()
    }

    fun editProfile(empName:String,empCueId:String,empDesignation:String,
                    iAddonCompleteListener: IAddonCompleteListener){
        Log.i("data","$empCueId,$empDesignation,$empName")
        return profile.editProfileInfo(empName,empCueId,empDesignation,iAddonCompleteListener)
    }
}