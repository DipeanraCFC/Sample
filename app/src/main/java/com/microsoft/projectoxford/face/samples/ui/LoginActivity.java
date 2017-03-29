package com.microsoft.projectoxford.face.samples.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microsoft.projectoxford.face.samples.R;

import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    EditText mEmailEditText, mPasswordEditText;
    Button mLoginButton, mRegisterButton;
    String email, password;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = (EditText) findViewById(R.id.activity_login_email_editText);
        mPasswordEditText = (EditText) findViewById(R.id.activity_login_password_editText);
        mLoginButton = (Button) findViewById(R.id.activity_login_login_button);
        mRegisterButton = (Button) findViewById(R.id.activity_login_register_button);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEmailEditText.getText().toString();
                password = mPasswordEditText.getText().toString();

                if (mEmailEditText.getText().toString().length() < 1) {
                    mEmailEditText.setError("Please Enter Email");
                } else if (mPasswordEditText.getText().toString().length() < 1) {
                    mPasswordEditText.setError("Please Enter Password");
                } else if (!isValidEmailID(mEmailEditText.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    //  mEmailEditText.setError("Please Enter Valid Email");
                } else {
                    signIn();
                }
            }
        });


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean isValidEmailID(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void signIn() {

        String Email = mEmailEditText.getText().toString().trim();
        String Password = mPasswordEditText.getText().toString().trim();

        if (mEmailEditText.getText().toString().length() < 1 || mPasswordEditText.getText().toString().length() < 1) {
            Toast.makeText(LoginActivity.this, "Please provide your email and password.", Toast.LENGTH_SHORT).show();
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches())) {
            Toast.makeText(LoginActivity.this, "Please provide valid email address.", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Signing in...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) { //login failed
                        progressDialog.dismiss();
                        //Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                    } else if (task.isSuccessful()) { //login successful
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successgul.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
            });
        }


    }
}

