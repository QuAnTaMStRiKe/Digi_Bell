package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up1.*

class SignUp1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up1)
        loginS.setOnClickListener {
            val i = Intent(this, Login1::class.java)
            startActivity(i)
        }
        signupcnfrm.setOnClickListener {
            val inte = Intent(this, HomeScreen1::class.java)
            startActivity(inte)
        }
    }
}