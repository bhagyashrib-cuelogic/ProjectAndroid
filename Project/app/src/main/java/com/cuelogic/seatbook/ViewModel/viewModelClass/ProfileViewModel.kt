package com.cuelogic.seatbook.ViewModel.viewModelClass

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.repository.firebaseManager.ProfileFirebaseData


class ProfileViewModel: ViewModel() {

    private var profile = ProfileFirebaseData()

    fun showProfile() : MutableLiveData<List<EmployeeData?>> {
        return profile.showProfileInfo()
    }

    fun editProfile():ProfileFirebaseData{
        return ProfileFirebaseData()
    }


}