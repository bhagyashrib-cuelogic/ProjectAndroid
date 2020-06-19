package com.cuelogic.seatbook.ViewModel.viewModelClass

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.model.SeatData
import com.cuelogic.seatbook.repository.firebaseManager.HomeFirebaseData


class HomeViewModel: ViewModel() {

    private var homeFirebaseData= HomeFirebaseData()


    fun showDate(date:String,flag:Int) : MutableLiveData<List<SeatData?>?> {
        return homeFirebaseData.showSeatDateWise(date,0)
    }

    fun saveBooking(checkTime: String,checkOut: String,reasonDescription: String,dateToCome: String,activity: FragmentActivity){
        return homeFirebaseData.saveDate(checkTime,checkOut,reasonDescription,dateToCome,activity)
    }

}