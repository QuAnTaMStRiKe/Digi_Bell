package com.example.digi_bell

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class Contact : AppCompatActivity() {
    lateinit var sensorEventListener: SensorEventListener
    lateinit var sensorManager: SensorManager
    lateinit var proxSensor: Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)



        sensorManager = getSystemService<SensorManager>()!!
        proxSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        sensorEventListener = object : SensorEventListener
        {
            override fun onSensorChanged(event: SensorEvent?) {
                Log.d("HWSENS", """
                 onSensorChanged: ${event!!.values[0]}
             """.trimIndent())
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // nothing
            }

        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorEventListener, proxSensor, 1000 * 1000
        )

    }

    override fun onPause() {
        //sensorManager.unregisterListener(sensorEventListener)
        super.onPause()
    }

}



//
//private lateinit var auth: FirebaseAuth
//private lateinit var dbRef: DatabaseReference
//private var firebaseUserID: String = ""
//
//auth = Firebase.auth
//firebaseUserID = auth.currentUser!!.uid
//dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
//backCon.setOnClickListener {
//
//    val i = Intent(this, UserProfile::class.java)
//    startActivity(i) }
//
//editCon.setOnClickListener {
//    updateContacts(contact1, contact2, contact3)
//}
//
//dbRef.child("Contacts").child("Contact1").addValueEventListener(object : ValueEventListener {
//    override fun onDataChange(snapshot: DataSnapshot) {
//        val num1 = snapshot.getValue(String::class.java)
//        contact1.setText(num1)
//    }
//
//    override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
//    }
//
//})
//dbRef.child("Contacts").child("Contact2").addValueEventListener(object : ValueEventListener {
//    override fun onDataChange(snapshot: DataSnapshot) {
//        val num2 = snapshot.getValue(String::class.java)
//        contact2.setText(num2)
//    }
//
//    override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
//    }
//
//})
//dbRef.child("Contacts").child("Contact3").addValueEventListener(object : ValueEventListener {
//    override fun onDataChange(snapshot: DataSnapshot) {
//        val num3 = snapshot.getValue(String::class.java)
//        contact3.setText(num3)
//    }
//
//    override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
//    }
//
//})
//




//
//private fun updateContacts(contact1: EditText, contact2: EditText, contact3: EditText) {
//    firebaseUserID = auth.currentUser!!.uid
//    dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
//    val builder = AlertDialog.Builder(this)
//    builder.setTitle("Update Contacts")
//    val inflater = LayoutInflater.from(this)
//    val view = inflater.inflate(R.layout.update_contacts, null)
//    val con1 = view.findViewById<EditText>(R.id.upCon1)
//    val con2 = view.findViewById<EditText>(R.id.upCon2)
//    val con3 = view.findViewById<EditText>(R.id.upCon3)
//    con1.text = contact1.editableText
//    con2.text = contact2.editableText
//    con3.text = contact3.editableText
//    builder.setView(view)
//    builder.setPositiveButton("Update"){ _, _ ->
//        val upCons1 = con1.text.toString().trim()
//        val upCons2 = con2.text.toString().trim()
//        val upCons3 = con3.text.toString().trim()
//        if(upCons1.isEmpty() ){
//            con1.error = "Please enter a number"
//            con1.requestFocus()
//            return@setPositiveButton
//        }
//        val userHashMap = HashMap<String, Any>()
//        userHashMap["Contact1"] = upCons1
//        userHashMap["Contact2"] = upCons2
//        userHashMap["Contact3"] = upCons3
//        dbRef.child("Contacts").setValue(userHashMap)
//        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
//        contact1.text = upCons1.toEditable()
//        contact2.text = upCons2.toEditable()
//        contact3.text = upCons3.toEditable()
//        Toast.makeText(this, "Number Updated", Toast.LENGTH_SHORT).show()
//    }
//    builder.setNegativeButton("No"){ _, _ ->
//
//    }
//    val alert = builder.create()
//    alert.show()
//}