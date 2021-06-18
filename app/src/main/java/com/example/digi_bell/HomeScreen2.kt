package com.example.digi_bell

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_screen2.*
import kotlinx.android.synthetic.main.help.*


class HomeScreen2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen2)

        hmbtn2.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
            dbRef.child("Help").removeValue()
            val i = Intent(this, HomeScreen1::class.java)
            startActivity(i)

        }
        val colorFrom = ContextCompat.getColor(this, R.color.red)
        val colorTo = ContextCompat.getColor(this, R.color.green)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 100 // milliseconds
        colorAnimation.repeatCount = 100
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.addUpdateListener { animator -> hmbtn2.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()

    }
}