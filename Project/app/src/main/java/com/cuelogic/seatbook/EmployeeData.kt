package com.cuelogic.seatbook

data class EmployeeData(var UID:String,var empName:String?=null, var emailAddress:String?=null,
                        var employeeDesignation:String?=null,val employeeProfile:String?=null,var isActive:String) {
}