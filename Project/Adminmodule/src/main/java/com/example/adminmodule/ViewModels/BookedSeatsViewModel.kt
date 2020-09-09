package com.example.adminmodule.ViewModels

import androidx.lifecycle.ViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.Repository.BookedSeatsRepo

class BookedSeatsViewModel :ViewModel(){

    var bookedSeatsRepo = BookedSeatsRepo()

    fun requestShowList():BookedSeatsRepo {
        return BookedSeatsRepo()
    }

    fun cancelBooking(info: BookingModel, iAddonCompleteListener: IAddonCompleteListener){
        return bookedSeatsRepo.cancelUserBookingSeat(info,iAddonCompleteListener)
    }

    fun updateSeatAfterCancelBooking(date:String){
        bookedSeatsRepo.updateSeatDataOnCancel(date)
    }
}