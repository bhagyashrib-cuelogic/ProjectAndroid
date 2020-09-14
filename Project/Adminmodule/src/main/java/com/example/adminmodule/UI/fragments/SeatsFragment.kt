package com.example.adminmodule.UI.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.model.BookingModel
import com.example.adminmodule.Observer.AddSeats
import com.example.adminmodule.Observer.AddSeatsObservable
import com.example.adminmodule.R
import com.example.adminmodule.UI.activities.ReserveSeatActivity
import com.example.adminmodule.Utilities.Utils
import com.example.adminmodule.ViewModels.BookedSeatsViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class SeatsFragment : Fragment(), AddSeats {

    private lateinit var bookingList: ArrayList<BookingModel>
    private lateinit var bookingListView: ListView
    lateinit var bookedSeatsViewModel: BookedSeatsViewModel
    lateinit var addSeatsObservable: AddSeatsObservable

    override fun onResume() {
        super.onResume()
        addSeatsObservable = AddSeatsObservable()
        addSeatsObservable.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        addSeatsObservable.unRegister(this)
    }

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seats, container, false)

        val context = container?.context!!
        bookingListView = view.findViewById(R.id.seatsListView)
        val addImage = view.findViewById<ImageView>(R.id.addIcon)!!

        bookedSeatsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application())
            .create(BookedSeatsViewModel::class.java)

        (activity as AppCompatActivity?)?.setSupportActionBar(customeToolbar)

        bookingList = ArrayList()
        val isConnected = Utils.isConnected(context)
        if (isConnected) {
            activity?.let {
                bookingList.clear()
                bookedSeatsViewModel.requestShowList()
                    .showUserCurrentBookingList(bookingList, bookingListView, it, object :
                        IAddonCompleteListener {
                        override fun addOnCompleteListener() {
                        }
                    })
            }
        }


        addImage.setOnClickListener {
            var intent = Intent(activity, ReserveSeatActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun addBooking(booking: BookingModel) {
        bookingList.clear()
        activity?.let {
            bookedSeatsViewModel.requestShowList()
                .showUserCurrentBookingList(bookingList, bookingListView, it, object :
                    IAddonCompleteListener {
                    override fun addOnCompleteListener() {
                    }
                })
        }

    }
}