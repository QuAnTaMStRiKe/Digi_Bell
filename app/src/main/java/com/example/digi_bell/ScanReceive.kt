package com.example.digi_bell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_scan_receive.*

class ScanReceive : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_receive)



            generate.setOnClickListener {
                val intent = Intent (this, Generator1::class.java )
                startActivity(intent)
            }



           scanner.setOnClickListener {
               val intentSc = Intent(this, Scanner::class.java)
               startActivity(intentSc)
           }

        }
    }
