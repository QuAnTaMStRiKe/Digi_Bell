package com.example.digi_bell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.activity_receiver.*

class Receiver : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        val senderID = "filg5JlG4IaNLmuVFyd6Qfq0f9s1"
        dbRef2 = FirebaseDatabase.getInstance().reference.child("Users").child(senderID)
        dbRef2.child("Help").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val help = snapshot.getValue(String::class.java)
                senderHelp.setText(help)
                if (senderHelp.text.isNotEmpty()){
                    Toast.makeText(this@Receiver, "Successfully Done", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}