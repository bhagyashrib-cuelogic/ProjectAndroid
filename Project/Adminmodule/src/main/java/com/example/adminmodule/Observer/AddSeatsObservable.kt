package com.example.adminmodule.Observer

import com.cuelogic.seatbook.model.BookingModel

class AddSeatsObservable : Observabel<AddSeats> {

    companion object {
        private var spinnerUserList: ArrayList<AddSeats> = ArrayList()
    }

    override fun register(t: AddSeats) {
        if(!spinnerUserList.contains(t)){
            spinnerUserList.add(t)
        }
    }

    override fun unRegister(t: AddSeats) {
        if(spinnerUserList.contains(t)){
            spinnerUserList.remove(t)
        }
    }

    fun notifyAddSeats(bookingModel: BookingModel){
        for(seats: AddSeats in spinnerUserList){
            seats.addBooking(bookingModel)
        }
    }

}