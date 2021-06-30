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
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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
import kotlinx.android.synthetic.main.activity_home_screen1.*
import kotlinx.android.synthetic.main.activity_receiver.*
import kotlinx.android.synthetic.main.toolbar_main.*


class Receiver : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef1: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private lateinit var dbRef3: DatabaseReference
    private var firebaseUserID: String = ""
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false
    lateinit var codescanner: CodeScanner
    private var name: String? = ""
    private var name2: String? = ""
    private var abdt: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid
        val list = arrayListOf<String>()
        val list2 = arrayListOf<String>()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list)
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list2)

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


//        bcmSender.setOnClickListener {
//            val bcmS = Intent(this, ScanReceive::class.java)
//            startActivity(bcmS)
//        }

        addId.setOnClickListener {
            name?.let { it1 -> list2.add(it1) }
            list.add(senderIDx.text.toString())
            lvSenderId.adapter = adapter
            lvSenderName.adapter = adapter2
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            senderIDx.text.clear()
            dataRef(list)
        }


        lvSenderName.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(this, "You Selected the name --> "+ list2[i], Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "You Selected the id --> "+ list[i], Toast.LENGTH_SHORT).show()
        }
        deleteId.setOnClickListener {
            val position: SparseBooleanArray = lvSenderName.checkedItemPositions
            val count = lvSenderName.count
            var item = count - 1
            val position2: SparseBooleanArray = lvSenderName.checkedItemPositions
            val count2 = lvSenderId.count
            var item2 = count2 - 1
            while (item >= 0 && item2 >= 0) {
                if (position.get(item) && position2.get(item2))
                {
                    adapter.remove(list[item2])
                    adapter2.remove(list2[item])
                }
                item--
                item2--
            }
            position.clear()
            adapter2.notifyDataSetChanged()
            position2.clear()
            adapter.notifyDataSetChanged()
        }

       audioON.setOnCheckedChangeListener{ _, isChecked ->
           if (isChecked){
               if(pause){
                   mediaPlayer.seekTo(mediaPlayer.currentPosition)
                   mediaPlayer.start()
                   pause = false

               }else{

                   mediaPlayer = MediaPlayer.create(applicationContext,R.raw.testtt)
                   mediaPlayer.start()


               }
               initializeSeekBar()

               mediaPlayer.setOnCompletionListener {

                   Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
               }
           }
           else{
               if(mediaPlayer.isPlaying || pause){
                   pause = false
                   seek_bar.progress = 0
                   mediaPlayer.stop()
                   mediaPlayer.reset()
                   mediaPlayer.release()
                   handler.removeCallbacks(runnable)


                   tv_pass.text = ""
                   tv_due.text = ""
                   Toast.makeText(this,"Call was dismissed",Toast.LENGTH_SHORT).show()
               }
           }
       }



//        bakSR.setOnClickListener {
//            val bakScRcv = Intent(this, ScanReceive::class.java)
//            startActivity(bakScRcv)
//        }
//
//        up3.setOnClickListener {
//            val usrPro = Intent(this, UserProfile::class.java)
//            startActivity(usrPro)
//        }



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

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        abdt = ActionBarDrawerToggle(
            this,
            drawer_layout1,toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        abdt!!.isDrawerIndicatorEnabled = true
        drawer_layout1!!.addDrawerListener(abdt!!)
        abdt!!.syncState()



        navigation1!!.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile1   -> {
                    val gotoAcc = Intent(this, Personal_info::class.java)
                    startActivity(gotoAcc)
                    overridePendingTransition(0, 0)
                    return@setNavigationItemSelectedListener true
                }

                R.id.bcmSender -> {
                    val gotoScRcv = Intent(this, ScanReceive::class.java)
                    startActivity(gotoScRcv)
                    overridePendingTransition(0, 0)
                    return@setNavigationItemSelectedListener true
                }
                R.id.logout1 -> {
                    FirebaseAuth.getInstance().signOut()
                    val log = Intent(this, Login::class.java)
                    startActivity(log)
                    overridePendingTransition(0, 0)
                    return@setNavigationItemSelectedListener true
                }
            }
            return@setNavigationItemSelectedListener true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (abdt!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item!!)
    }


    private fun dataRef(list: ArrayList<String>) {

      list.forEachIndexed { index, _ ->
          dbRef2 = FirebaseDatabase.getInstance().reference.child("Users").child(list[index])
          dbRef3 = FirebaseDatabase.getInstance().reference.child("Users").child(list[index])
          dbRef2.child("Help").addValueEventListener(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  val help = snapshot.getValue(String::class.java)
                  helpReceived.setText(help)
                  val h = "1234"
                  if(helpReceived.text.toString() == h){
                      Log.e("ON", "${helpReceived.text} + $h")
                      dbRef3.child("Name").addValueEventListener(object : ValueEventListener{
                          override fun onDataChange(snapshot: DataSnapshot) {
                              name2 = snapshot.getValue(String::class.java)
                              Toast.makeText(this@Receiver," Call from $name2 incoming ",Toast.LENGTH_SHORT).show()
                          }

                          override fun onCancelled(error: DatabaseError) {
                              TODO("Not yet implemented")
                          }

                      })

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

                Toast.makeText(this, "Scan Completed click on Add to add in the list", Toast.LENGTH_SHORT).show()
                senderIDx.setText(it.text)
                dbRef1 = FirebaseDatabase.getInstance().reference.child("Users").child(senderIDx.text.toString())
                dbRef1.child("Name").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        name = snapshot.getValue(String::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
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