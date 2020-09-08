package com.example.adminmodule.Observer

interface Observabel<T> {
    fun register(t: T)
    fun  unRegister(t: T)
}