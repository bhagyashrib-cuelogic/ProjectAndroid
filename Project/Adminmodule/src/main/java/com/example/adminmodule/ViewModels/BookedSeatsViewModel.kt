package com.example.adminmodule.ViewModels

import androidx.lifecycle.ViewModel
import com.example.adminmodule.Repository.BookedSeatsRepo

class BookedSeatsViewModel :ViewModel(){


    fun requestShowList():BookedSeatsRepo {
        return BookedSeatsRepo()
    }
}