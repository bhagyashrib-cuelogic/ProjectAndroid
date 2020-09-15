package com.example.adminmodule.ViewModels

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adminmodule.Models.SeatData
import com.example.adminmodule.Repository.BookedSeatsRepo
import com.example.adminmodule.Repository.LoginRepo
import com.example.adminmodule.Repository.ReasonsRepo

class LoginViewModel :ViewModel(){
    private var loginRepo = LoginRepo()


//    fun authenticateEmail(userEmail: String,activity: Activity): Boolean {
//        return loginRepo.authenticateEmail(userEmail,activity)
//    }

    fun doLogin(): LoginRepo {
        return LoginRepo()
    }
}