package com.cuelogic.seatbook.ViewModel.viewModelClass

import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.SeatData
import com.cuelogic.seatbook.repository.firebaseManager.HomeFirebaseData


class HomeViewModel: ViewModel() {

    private var homeFirebaseData= HomeFirebaseData()


    fun showDate(date:String,flag:Int) : MutableLiveData<List<SeatData?>?> {
        return homeFirebaseData.showSeatDateWise(date,0)
    }

    fun saveBooking(activity: FragmentActivity,dateToCome: String,checkTime: String,checkOut: String,reasonDescription: String,
                    iAddonCompleteListener: IAddonCompleteListener){
        return homeFirebaseData.checkBookedSeatForParticularDateByUser(activity,dateToCome,checkTime,checkOut,
            reasonDescription,iAddonCompleteListener)
    }

    fun getReason(spinnerDataList:ArrayList<String>,adapter:ArrayAdapter<String>){
        return homeFirebaseData.getReasonData(spinnerDataList,adapter)
    }

}