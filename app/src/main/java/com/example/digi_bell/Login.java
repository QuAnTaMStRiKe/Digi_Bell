package com.example.digi_bell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn, tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



            mFirebaseAuth = FirebaseAuth.getInstance();
            emailId = findViewById(R.id.emailIdL);
            password = findViewById(R.id.passwordL);
            btnSignIn = findViewById(R.id.loginCnfrmx);
            tvSignUp = findViewById(R.id.signuplx);

            mAuthStateListener = firebaseAuth -> {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(Login.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, ScanReceive.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(Login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            };

            btnSignIn.setOnClickListener(v -> {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, task -> {
                        if(!task.isSuccessful()){
                            Toast.makeText(Login.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intToHome = new Intent(Login.this,ScanReceive.class);
                            startActivity(intToHome);
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            });

            tvSignUp.setOnClickListener(v -> {
                Intent intSignUp = new Intent(Login.this, SignUp.class);
                startActivity(intSignUp);
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}