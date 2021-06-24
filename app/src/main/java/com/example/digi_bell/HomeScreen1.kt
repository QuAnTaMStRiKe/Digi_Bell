package com.example.digi_bell


import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_screen1.*


class HomeScreen1 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen1)


        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid

        goToqr.setOnClickListener {
            val qr = Intent(this, Generator1::class.java)
            startActivity(qr)
        }

        chngRcv.setOnClickListener {
            val rcv = Intent(this, ScanReceive::class.java)
            startActivity(rcv)
        }

        backGen.setOnClickListener {
            val intentSr = Intent(this, Generator1::class.java)
            startActivity(intentSr)
        }

        hmbtn1.setOnClickListener {
           val help:String = "1234"
            Log.d("help", " help")
           sendHelp(help)

            val vibrationEffect1: VibrationEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationEffect1 =
                    VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.cancel()
                vibrator.vibrate(vibrationEffect1)
            }
            val i = Intent(this, HomeScreen2::class.java)
            startActivity(i)
       }
        up1.setOnClickListener {
            val int = Intent(this, UserProfile::class.java)
            startActivity(int)
        }
    }


    private fun sendHelp(sendHelp: String){
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        dbRef.child("Help").setValue(sendHelp)
        Toast.makeText(this, "Asking Help", Toast.LENGTH_SHORT).show()
    }

}