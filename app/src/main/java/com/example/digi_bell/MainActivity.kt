package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TIME_OUT = 400
        Handler().postDelayed(Runnable {
            val inte = Intent(this, SignUp::class.java)
            startActivity(inte)
            finish()
        }, TIME_OUT.toLong())
    }
}