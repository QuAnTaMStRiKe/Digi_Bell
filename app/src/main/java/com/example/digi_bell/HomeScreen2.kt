package com.example.digi_bell

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home_screen2.*


class HomeScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen2)
        val TIME_OUT = 10
        hmbtn2.setOnClickListener {
            val i = Intent(this, HomeScreen1::class.java)
            startActivity(i)
        }
//        Handler().postDelayed(Runnable {
//            val inte = Intent(this, HomeScreen3::class.java)
//            startActivity(inte)
//            finish()
//        }, TIME_OUT.toLong())
    }
}