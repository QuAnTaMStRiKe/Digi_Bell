package com.example.digi_bell

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfile : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    private val sharedPrefFile = "SharedPref"
    private var sendid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        pinfo.setOnClickListener {
            val i = Intent(this, Personal_info::class.java)
            startActivity(i)
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
            sendid = sharedPreferences.getString("Snd"," ")
            if (sendid == "Sender"){
            val bak = Intent(this, HomeScreen1::class.java)
            startActivity(bak)
            } else if(sendid == "Receiver"){
                val bakRec = Intent(this, Receiver::class.java)
                startActivity(bakRec)
            }
        }
        hm2.setOnClickListener {
            sendid = sharedPreferences.getString("Snd"," ")
            if (sendid == "Sender"){
                val bak = Intent(this, HomeScreen1::class.java)
                startActivity(bak)
            } else if(sendid == "Receiver"){
                val bakRec = Intent(this, Receiver::class.java)
                startActivity(bakRec)
            }
        }
    }
}