package com.cuelogic.seatbook.preferences

import android.content.Context
import android.content.SharedPreferences

class User(private var context: Context) {

    private lateinit var sharedPreferences: SharedPreferences

    fun getIsLogin(): Boolean {
        sharedPreferences = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLogin", false)
    }

    fun setUId(isLogin: Boolean) {
        sharedPreferences = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLogin", isLogin).apply()
    }
}