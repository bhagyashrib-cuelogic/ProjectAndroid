package com.cuelogic.seatbook

data  class BookingData (var id:String,var date:String,var CheckInTime:String,var CheckOutTime:String,var Reason:String,val status:String,val isBooked:Int){
    public constructor():this("","3march","10AM","7PM","Others","Booked",0)
}