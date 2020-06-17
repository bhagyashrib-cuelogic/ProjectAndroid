package com.cuelogic.seatbook.ViewModel.viewModelClass

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.callback.ProfileListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.model.EmployeeData
import com.cuelogic.seatbook.repository.firebaseManager.ProfileFirebaseData


class ProfileViewModel {

    var empName: String? = null
    var empEmail: String? = null
    var empCue: String? = null
    var empDesg: String? = null

    fun onProfileSaveClick(view: View) {

//        val iAddonCompleteListener:IAddonCompleteListener
//        var userInfo=ProfileFirebaseData().editProfileInfo(empName!!,empEmail!!,empCue!!,empDesg!!,iAddonCompleteListener)
    }

}