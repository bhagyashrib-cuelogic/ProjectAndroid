package com.cuelogic.seatbook.fragment

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.cuelogic.seatbook.R
import com.cuelogic.seatbook.ViewModel.viewModelClass.HistoryViewModel
import com.cuelogic.seatbook.activies.MainActivity
import com.cuelogic.seatbook.activies.ProfileActivity
import com.cuelogic.seatbook.callback.IAddonCompleteListener
import com.cuelogic.seatbook.callback.ProfileListener
import com.cuelogic.seatbook.model.BookingData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() ,ProfileListener,IAddonCompleteListener{

    private lateinit var userList : MutableList<BookingData>
    private lateinit var listView : ListView
    private lateinit var auth: FirebaseAuth
    lateinit var viewModel: HistoryViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container,false)

        (activity as AppCompatActivity?)?.setSupportActionBar(customeToolbar)
        val profile = view.findViewById<ImageView>(R.id.profile_icon)
        profile.setOnClickListener {
            var intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Application()).create(HistoryViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        val currentUser = user.uid!!

        val calendarInstance = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(calendarInstance)
      //  val noDataText = view.findViewById<TextView>(R.id.noText)


        listView = view.findViewById(R.id.listViewItem)
        userList = mutableListOf()
        val context: FragmentActivity? = activity

            viewModel.fetchSampleList()
                .showHistory(currentDate, currentUser, userList, context, listView,object :IAddonCompleteListener{
                    override fun addOnCompleteListener() {

                    }
                })
            addOnCompleteListener()
        return view
    }
    override fun onSuccess(v: String) {
        Toast.makeText(activity,"$v",Toast.LENGTH_SHORT).show()
}

    override fun onFail() {
        TODO("Not yet implemented")
    }

    override fun addOnCompleteListener() {
       if (viewModel==null){
           Toast.makeText(activity,"eeeeee",Toast.LENGTH_SHORT).show()
       }
    }
}
