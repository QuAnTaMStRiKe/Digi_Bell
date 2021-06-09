package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home_screen1.*

class HomeScreen1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen1)
        hmbtn1.setOnClickListener {
            val i = Intent(this, HomeScreen2::class.java)
            startActivity(i)
        }
    }
}