package com.cacaomovil.app.activities

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cacaomovil.app.R

class Splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3500
    private lateinit var Session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        Session = getSharedPreferences("datos", MODE_PRIVATE);

        Handler().postDelayed(Runnable {
            val selectStation = Intent(this@Splash, HomeActivity::class.java)
            startActivity(selectStation)

            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
