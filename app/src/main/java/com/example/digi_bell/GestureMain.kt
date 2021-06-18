package com.example.digi_bell

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gesture_main.*


class GestureMain : AppCompatActivity() {


    private val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_main)



        backGes.setOnClickListener {
            val i = Intent(this, UserProfile::class.java)
            startActivity(i)
        }

        gesOn.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                //onKeyLongPress()
            }else{

            }
        }
    }
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_POWER) {
            val vibrationEffect1: VibrationEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationEffect1 =
                    VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.cancel()
                vibrator.vibrate(vibrationEffect1)
            }
            val i = Intent(this, HomeScreen2::class.java)
            startActivity(i)
            true
        } else super.onKeyLongPress(keyCode, event)
    }


}