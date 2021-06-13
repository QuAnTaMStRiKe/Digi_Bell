package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gesture_main.*

class GestureMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_main)
        backGes.setOnClickListener {
            val i = Intent(this, UserProfile::class.java)
            startActivity(i)
        }
    }
}