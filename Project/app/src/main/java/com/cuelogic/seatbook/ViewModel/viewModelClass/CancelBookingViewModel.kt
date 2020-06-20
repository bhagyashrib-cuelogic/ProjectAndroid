package com.cuelogic.seatbook.ViewModel.viewModelClass

import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingData
import com.cuelogic.seatbook.repository.firebaseManager.FirebaseOperation


class CancelBookingViewModel: ViewModel() {


    var firebaseOperation = FirebaseOperation()


    fun cancelBooking(info:BookingData,iAddonCompleteListener: IAddonCompleteListener){
           return firebaseOperation.cancelUserBookingSeat(info,iAddonCompleteListener)
    }

    fun updateSeatAfterCancelBooking(date:String){
        firebaseOperation.updateSeatDataOnCancel(date)
    }


}