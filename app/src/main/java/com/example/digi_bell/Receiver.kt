package com.example.digi_bell


import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_receiver.*


class Receiver : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef2: DatabaseReference
    private var firebaseUserID: String = ""
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false
    lateinit var codescanner: CodeScanner
    private var sendid: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid
        val list = arrayListOf<String>()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list)


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                123
            )
        } else {
            startScanning()

        }


        addId.setOnClickListener {
            list.add(senderIDx.text.toString())
            lvSenderId.adapter = adapter
            adapter.notifyDataSetChanged()
            senderIDx.text.clear()
            dataRef(list)
        }


        lvSenderId.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(this, "You Selected the item --> "+ list[i], Toast.LENGTH_SHORT).show()
        }
        deleteId.setOnClickListener {
            val position: SparseBooleanArray = lvSenderId.checkedItemPositions
            val count = lvSenderId.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                    adapter.remove(list[item])
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }

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

    private fun dataRef(list: ArrayList<String>) {

      list.forEachIndexed { index, s ->
          dbRef2 = FirebaseDatabase.getInstance().reference.child("Users").child(list[index])
          dbRef2.child("Help").addValueEventListener(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  val help = snapshot.getValue(String::class.java)
                  helpReceived.setText(help)
                  val h = "1234"
                  if(helpReceived.text.toString() == h){
                      Log.e("ON", "${helpReceived.text} + $h")
                      audioON.isVisible= true
                      audioON.isChecked = true
                  }
                  else{
                      audioON.isVisible = false
                      audioON.isChecked = false
                  }
              }
              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }

          })
      }
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

    private fun startScanning() {

        val scannerView: CodeScannerView = findViewById(R.id.scanner_view_rec)
        codescanner = CodeScanner(this, scannerView)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS
        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true
        codescanner.isFlashEnabled = false
        codescanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                Toast.makeText(this, "Scan Result: ${it.text}", Toast.LENGTH_SHORT).show()
                senderIDx.setText(it.text)
                sendid = senderIDx.text.toString()

            }
        }
        codescanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Camera Initialization Error: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        scannerView.setOnClickListener {
            codescanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codescanner.isInitialized)
        {
            codescanner?.startPreview()
        }
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        if (::codescanner.isInitialized)
        {
            codescanner?.releaseResources()
        }

        super.onPause()
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