package com.example.digi_bell


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.sql.Types.NULL
import java.util.*
import kotlin.collections.HashMap


class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        showPass.setOnClickListener {
        }
        showPass.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showPass.setBackgroundResource(R.drawable.ic_openeye)
                password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                showPass.hint = "Password"
                showPass.textOn = NULL.toString()
                showPass.textOff = NULL.toString()
            } else {
                showPass.setBackgroundResource(R.drawable.ic_eye)
                showPass.hint = "Password"
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password.setSelection(password.text.length)
            }
        }
        signupcnfrmx.setOnClickListener {
            val email = emailId.text.toString()
            val pwd = password.text.toString()
            if (email.isEmpty()) {
                emailId.error = "Please enter email id"
                emailId.requestFocus()
            }
            else if (pwd.isEmpty()){
                password.error = "Please enter your password"
                password.requestFocus()
            }
            else if(email.isEmpty() && pwd.isEmpty()){
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show()
            }
            else if(pwd != passwordCnfrm.text.toString()){
                Toast.makeText(this, "Please confirm your password and enter it again", Toast.LENGTH_SHORT).show()
            }
            else if(!(email.isEmpty() && pwd.isEmpty())){
                auth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = auth.currentUser!!.uid
                        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["Name"] = name.text.toString()
                        userHashMap["Number"] = number.text.toString()
                        userHashMap["Email Id"] = emailId.text.toString()
                        userHashMap["Password"] = password.text.toString()
                        dbRef.updateChildren(userHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val i = Intent(this, HomeScreen1::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                            finish()
                            Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()

                        }
                        }

                    } else {
                        Toast.makeText(this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }
        loginSx.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }
    }
}