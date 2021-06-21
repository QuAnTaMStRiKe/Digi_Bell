package com.example.digi_bell

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_receiver.*
import kotlinx.android.synthetic.main.activity_scanner.*

class Scanner : AppCompatActivity() {

    private val sharedPrefFile = "SharedPref"

    lateinit var codescanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)


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

        sendToReceive.setOnClickListener {
            val sndRec = Intent(this, Receiver::class.java)
            startActivity(sndRec)
        }
    }

    private fun startScanning() {
         val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
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
               rsltSender.setText(it.text)
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putString("id",it.text)
                editor.apply()
                editor.commit()
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
