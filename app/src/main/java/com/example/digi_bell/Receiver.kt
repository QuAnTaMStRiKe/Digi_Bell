package com.example.digi_bell

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_generator1.*
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.activity_receiver.*
import kotlinx.android.synthetic.main.activity_scanner.*

class Receiver : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef2: DatabaseReference
    private var firebaseUserID: String = ""
    private val sharedPrefFile = "kotlinsharedpreference"

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
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}