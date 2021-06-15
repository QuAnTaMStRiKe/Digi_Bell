package com.example.digi_bell

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfile : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    private val authx: AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        pinfo.setOnClickListener {
            val i = Intent(this, Personal_info::class.java)
            startActivity(i)
        }
        cont.setOnClickListener {
            val j = Intent(this, Contact::class.java)
            startActivity(j)
        }
        gesture.setOnClickListener {
            val ges = Intent(this, GestureMain::class.java)
            startActivity(ges)
        }
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val log = Intent(this, Login::class.java)
            startActivity(log)
        }
        backUI.setOnClickListener {
            val bak = Intent(this, HomeScreen1::class.java)
            startActivity(bak)
        }
        hm2.setOnClickListener {
            val bakhm = Intent(this, HomeScreen1::class.java)
            startActivity(bakhm)
        }
    }
}