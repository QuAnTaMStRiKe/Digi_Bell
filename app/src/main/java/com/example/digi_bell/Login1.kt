package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login1.*

class Login1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)
        signupl.setOnClickListener {
            val i = Intent(this, SignUp1::class.java)
            startActivity(i)
        }
        loginCnfrm.setOnClickListener {
            val inte = Intent(this, HomeScreen1::class.java)
            startActivity(inte)
        }
    }
}