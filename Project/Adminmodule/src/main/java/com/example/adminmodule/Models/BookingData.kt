package com.cuelogic.seatbook.model

data class BookingData  (
    val isBooked: Int,
    var CheckInTime: String,
    var CheckOutTime: String,
    var date: String,
    var id: String,
    var Reason: String,
    val status: String
) {

    constructor() : this(
        isBooked = 0,
        CheckInTime = "",
        CheckOutTime = "",
        date = "",
        id = "",
        Reason = "",
        status = ""

    )
}