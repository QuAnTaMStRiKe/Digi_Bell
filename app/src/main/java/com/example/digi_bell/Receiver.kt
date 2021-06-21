package com.example.digi_bell

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_receiver.*


class Receiver : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef2: DatabaseReference
    private var firebaseUserID: String = ""
    private val sharedPrefFile = "SharedPref"
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid

         val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val sendid = sharedPreferences.getString("id"," ")
        senderIDx.setText(sendid)
        dbRef2 = sendid?.let { FirebaseDatabase.getInstance().reference.child("Users").child(it) }!!
        dbRef2.child("Help").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val help = snapshot.getValue(String::class.java)
                helpReceived.setText(help)
                val h = "1234"
                if(helpReceived.text.toString() == h){
                    Log.e("ON", "${helpReceived.text} + $h")
                    audioON.isChecked = true
                }
                else{
                    audioON.isChecked = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



       audioON.setOnCheckedChangeListener{ _, isChecked ->
           if (isChecked){
               if(pause){
                   mediaPlayer.seekTo(mediaPlayer.currentPosition)
                   mediaPlayer.start()
                   pause = false
                   Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()
               }else{

                   mediaPlayer = MediaPlayer.create(applicationContext,R.raw.testtt)
                   mediaPlayer.start()
                   Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()

               }
               initializeSeekBar()

               mediaPlayer.setOnCompletionListener {

                   Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
               }
           }
           else{
               if(mediaPlayer.isPlaying || pause.equals(true)){
                   pause = false
                   seek_bar.setProgress(0)
                   mediaPlayer.stop()
                   mediaPlayer.reset()
                   mediaPlayer.release()
                   handler.removeCallbacks(runnable)


                   tv_pass.text = ""
                   tv_due.text = ""
                   Toast.makeText(this,"media stop",Toast.LENGTH_SHORT).show()
               }
           }
       }



        bakSR.setOnClickListener {
            val bakScRcv = Intent(this, ScanReceive::class.java)
            startActivity(bakScRcv)
        }

        up3.setOnClickListener {
            val usrPro = Intent(this, UserProfile::class.java)
            startActivity(usrPro)
        }



        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })


    }

    private fun initializeSeekBar() {
        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            tv_pass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            tv_due.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition/1000
    }