package com.cuelogic.seatbook.ViewModel.viewModelClass

import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.callback.ProfileListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.repository.firebaseManager.HistoryFirebaseData

class HistoryViewModel:ViewModel() {

    var profileListener : ProfileListener?=null

    fun fetchSampleList():HistoryFirebaseData {
        profileListener?.onSuccess("hhhh")
        return HistoryFirebaseData()
    }

}