package com.example.digi_bell

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_home_screen2.*


class HomeScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen2)

        hmbtn2.setOnClickListener {
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