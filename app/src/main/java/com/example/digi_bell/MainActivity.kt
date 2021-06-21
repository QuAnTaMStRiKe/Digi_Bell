package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TIME_OUT = 400
        Handler().postDelayed({
            if(auth.currentUser == null){
            val inte = Intent(this, SignUp::class.java)
            startActivity(inte)
            finish()}
            else{
                val intent = Intent(this, ScanReceive::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT.toLong())
    }
}