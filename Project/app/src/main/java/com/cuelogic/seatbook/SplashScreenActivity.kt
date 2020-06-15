package com.cuelogic.seatbook

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {

    private val splashTime :Long= 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val activeNetwork =
                (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

                var session = User(MainActivity@ this)
                var value = session.getUId()
                if (value != "") {
                    startActivity(
                        Intent(this, SeatBookActivity::class.java)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }
            }else
            {
                Toast.makeText(this,"Please check your network connection",Toast.LENGTH_LONG).show()
            }
        },splashTime)

    }
}
