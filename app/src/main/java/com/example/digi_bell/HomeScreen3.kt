package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_home_screen2.*
import kotlinx.android.synthetic.main.activity_home_screen3.*

class HomeScreen3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen3)
        val TIME_OUT = 10
        hmbtn3.setOnClickListener {
            val i = Intent(this, HomeScreen1::class.java)
            startActivity(i)
        }
//        Handler().postDelayed(Runnable {
//            val inte = Intent(this, HomeScreen2::class.java)
//            startActivity(inte)
//            finish()
//        }, TIME_OUT.toLong())
    }
}