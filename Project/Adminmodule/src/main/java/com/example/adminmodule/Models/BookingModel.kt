package com.cuelogic.seatbook.model

data class BookingModel(
    var empName:String,
    var id: String,
    var date: String,
    var CheckInTime: String,
    var CheckOutTime: String,
    var Reason: String,
    val status: String,
    val isBooked: Int
) {

    constructor() : this(
        empName = "",
        id = "",
        date = "",
        CheckInTime = "",
        CheckOutTime = "",
        Reason = "",
        status = "",
        isBooked = 0

    )
}