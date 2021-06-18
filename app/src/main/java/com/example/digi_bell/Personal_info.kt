package com.example.digi_bell

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.name_update.*


class Personal_info : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        auth = Firebase.auth
        firebaseUserID = auth.currentUser!!.uid
        val senderId = "filg5JlG4IaNLmuVFyd6Qfq0f9s1"
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        dbRef2 = FirebaseDatabase.getInstance().reference.child("Users").child(senderId)
        backPI.setOnClickListener {
            val i = Intent(this, UserProfile::class.java)
            startActivity(i)
        }

        dbRef.child("Name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nameD = snapshot.getValue(String::class.java)
                namePI.setText(nameD) 
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        dbRef.child("Number").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val numberD = snapshot.getValue(String::class.java)
                numberPI.setText(numberD)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        dbRef.child("Email Id").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailD = snapshot.getValue(String::class.java)
                emailPI.setText(emailD)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        dbRef2.child("Help").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val help = snapshot.getValue(String::class.java)
                helpReceived.setText(help)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        nameEdit.setOnClickListener {
            showUpdateName(namePI)
        }
        numberEdit.setOnClickListener {
            showUpdateNumber(numberPI)
        }
        emailEdit.setOnClickListener {
            showUpdateEmail(emailPI)
        }

    }
    private fun showUpdateEmail(emailPI: EditText) {
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Email")
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.email_update, null)
        val editText = view.findViewById<EditText>(R.id.emailEN)
        editText.text = emailPI.editableText
        builder.setView(view)
        builder.setPositiveButton("Update"){ _, _ ->
            val name = editText.text.toString().trim()
            if(name.isEmpty()){
                editText.error = "Please enter a Email Id"
                editText.requestFocus()
                return@setPositiveButton
            }
            dbRef.child("Email Id").setValue(name)
            fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
            emailPI.text = name.toEditable()
            Toast.makeText(this, "Email Updated", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

    private fun showUpdateNumber(numberPI: EditText) {
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Number")
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.number_update, null)
        val editText = view.findViewById<EditText>(R.id.numberEN)
        editText.text = numberPI.editableText
        builder.setView(view)
        builder.setPositiveButton("Update"){ _, _ ->
            val name = editText.text.toString().trim()
            if(name.isEmpty()){
                editText.error = "Please enter a number"
                editText.requestFocus()
                return@setPositiveButton
            }
            dbRef.child("Number").setValue(name)
            fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
            numberPI.text = name.toEditable()
            Toast.makeText(this, "Number Updated", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

    private fun showUpdateName(namePI: EditText) {
        firebaseUserID = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Name")
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.name_update, null)
        val editText = view.findViewById<EditText>(R.id.nameEN)
        editText.text = namePI.editableText
        builder.setView(view)
        builder.setPositiveButton("Update"){ _, _ ->
            val name = editText.text.toString().trim()
            if(name.isEmpty()){
                editText.error = "Please enter a name"
                editText.requestFocus()
                return@setPositiveButton
            }
            dbRef.child("Name").setValue(name)
            fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
            namePI.text = name.toEditable()
            Toast.makeText(this, "Name Updated", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

}