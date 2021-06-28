package com.example.digi_bell


import android.content.Intent
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_screen1.*


class HomeScreen1 : AppCompatActivity() {


    private var abdt: ActionBarDrawerToggle? = null



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


        abdt = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        abdt!!.isDrawerIndicatorEnabled = true
        drawer_layout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        navigation!!.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    Log.e("Account", "My Account Clicked")
                    Toast.makeText(this, "My Account", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                R.id.qrCode -> {
                    Toast.makeText(this, "Show QR Code", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                R.id.bcmReceiver -> {
                    Toast.makeText(this, "Become Receiver", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
            }
             return@setNavigationItemSelectedListener true
        }

    }


    private fun sendHelp(sendHelp: String){
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        dbRef.child("Help").setValue(sendHelp)
        Toast.makeText(this, "Asking Help", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (abdt!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item!!)
    }


}