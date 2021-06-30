
package com.example.digi_bell

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_gesture_main.*
import java.util.*
import kotlin.math.sqrt


class GestureMain : AppCompatActivity() {




    private var sensorManager1: SensorManager? = null
    var count = -1
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var counter = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_main)

        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid
        val sharedPrefs = getSharedPreferences("SharedPref", MODE_PRIVATE)
        gesOn.isChecked = sharedPrefs.getBoolean("Checked", false)
        ges2On.isChecked = sharedPrefs.getBoolean("Checked2", false)

     gesOn.setOnCheckedChangeListener { _, isChecked ->
         if(isChecked){
             val editor = getSharedPreferences("SharedPref", MODE_PRIVATE).edit()
             editor.putBoolean("Checked", true)
             editor.apply()
             sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
             Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
                 .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
             acceleration = 10f
             currentAcceleration = SensorManager.GRAVITY_EARTH
             lastAcceleration = SensorManager.GRAVITY_EARTH
         } else{
             val editor = getSharedPreferences("SharedPref", MODE_PRIVATE).edit()
             editor.putBoolean("Checked", false)
             editor.apply()
             dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
             dbRef.child("Help").removeValue()

          //   sensorManager!!.unregisterListener(sensorListener)
         }
     }



        ges2On.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){

                val editor = getSharedPreferences("SharedPref", MODE_PRIVATE).edit()
                editor.putBoolean("Checked2", true)
                editor.apply()
                sensorManager1 = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                Objects.requireNonNull(sensorManager1)!!.registerListener(sensorListener1, sensorManager1!!
                    .getDefaultSensor(Sensor.TYPE_PROXIMITY), 1000 * 1000)

            }else{
                val editor = getSharedPreferences("SharedPref", MODE_PRIVATE).edit()
                editor.putBoolean("Checked2", false)
                editor.apply()
                dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                dbRef.child("Help").removeValue()
                sensorManager1!!.unregisterListener(sensorListener1)
            }
        }

        backGes.setOnClickListener {
            val inte = Intent(this, HomeScreen1::class.java)
            startActivity(inte)
            overridePendingTransition(0, 0)
        }

    }


    private val sensorListener: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {

            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            val x = event.values[0]
            val y = event.values[1]
            //   val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y).toDouble()).toFloat()

            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            var curTime = System.currentTimeMillis()
            // only allow one update every 100ms.
            var lastUpdate = 0
            if ((curTime - lastUpdate) > 100) {
                var diffTime = (curTime - lastUpdate)
                lastUpdate = curTime.toInt();
                if (acceleration > 20) {
                    counter++
                    Log.e("Counter", "$counter")

                }

                if (counter == 3) {
                    val help: String = "1234"
                    Log.d("help", " help")
                    sendHelp(help)
                    val vibrationEffect1: VibrationEffect
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrationEffect1 =
                            VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                        vibrator.cancel()
                        vibrator.vibrate(vibrationEffect1)
                    }
                    val i = Intent(this@GestureMain, HomeScreen2::class.java)
                    startActivity(i)
                    Toast.makeText(applicationContext, "Shake phone", Toast.LENGTH_SHORT).show()
                    counter = 0
                }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }
    private val sensorListener1: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (event!!.values[0] > 0)
            {
                Log.e("Count", "$count")
                count++

            }

            if (count == 3) {
               Log.e("Complete", "Three Counts")
                val help: String = "1234"
                Log.d("help", " help")
                sendHelp(help)
                val vibrationEffect1: VibrationEffect
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrationEffect1 =
                        VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.cancel()
                    vibrator.vibrate(vibrationEffect1)
                }
                val i = Intent(this@GestureMain, HomeScreen2::class.java)
                startActivity(i)
                Toast.makeText(applicationContext, "Shake phone", Toast.LENGTH_SHORT).show()
                count = 0
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

    }


    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
        sensorManager1?.registerListener(sensorListener1, sensorManager1!!.getDefaultSensor(
            Sensor.TYPE_PROXIMITY), 1000 * 1000
        )
    }
    override fun onPause() {
       // sensorManager!!.unregisterListener(sensorListener)
        //  sensorManager1?.unregisterListener(this)
        super.onPause()
    }

    private fun sendHelp(sendHelp: String){
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        dbRef.child("Help").setValue(sendHelp)
        Toast.makeText(this, "Asking Help", Toast.LENGTH_SHORT).show()
    }


}




