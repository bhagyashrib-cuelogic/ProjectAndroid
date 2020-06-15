package com.cuelogic.seatbook.model

data class BookingData(
    var id: String,
    var date: String,
    var CheckInTime: String,
    var CheckOutTime: String,
    var Reason: String,
    val status: String,
    val isBooked: Int
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        0
    )
}