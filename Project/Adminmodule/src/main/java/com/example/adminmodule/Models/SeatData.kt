package com.example.adminmodule.Models

data class SeatData(var booked:Int,var total:Int,var available:Int,var date:String) {
    constructor():this(0,200,200,"")
}