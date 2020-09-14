package com.example.adminmodule.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cuelogic.seatbook.preferences.User
import com.example.adminmodule.R


class SplashScreenActivity : AppCompatActivity() {

    private val splashTime: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val session =
                User(this)
            val value = session.getIsLogin()
            if (value) {
                startActivity(
                    Intent(this, HomeScreenActivity::class.java)
                )
                finish()
            } else {
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
                finish()
            }
        }, splashTime)
    }
}
