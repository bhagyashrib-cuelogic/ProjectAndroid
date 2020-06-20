package com.cuelogic.seatbook.ViewModel.viewModelClass

import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.repository.firebaseManager.RequestFirebaseData

class RequestViewModel:ViewModel() {

    var request = RequestFirebaseData()

    fun requestShowList():RequestFirebaseData {
        return RequestFirebaseData()
    }


}