package com.cuelogic.seatbook.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.HistoryViewModel
import com.cuelogic.seatbook.ViewModel.viewModelClass.RequestViewModel
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.callback.ProfileListener
import com.cuelogic.seatbook.repository.firebaseManager.RequestFirebaseData
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class RequestFragment : Fragment() {

    private lateinit var userList: ArrayList<BookingData>
    private lateinit var listView: ListView
    lateinit var viewModel: RequestViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request, container, false)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application()).create(RequestViewModel::class.java)

        userList = ArrayList()
        listView = view.findViewById(R.id.listViewItem)

        activity?.let {
           viewModel.requestShowList().showUserCurrentBookingList(userList,listView, it,object :
                IAddonCompleteListener {
                override fun addOnCompleteListener() {
                    Toast.makeText(activity,"Request", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }
}

