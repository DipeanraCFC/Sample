package com.microsoft.projectoxford.face.samples.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.microsoft.projectoxford.face.samples.POJO.User;
import com.microsoft.projectoxford.face.samples.R;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    EditText mNameEditText, mEmailEditText, mPasswordEditText, mAddressEditText, mPhoneEditText;
    Button mRegisterButton, mLoginButton;
    ProgressDialog mProgressDialog;
    String name, email, password, phone, address;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseMain;
    private StorageReference mStorageRef;
    private StorageReference riversRef;
    Uri file;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mNameEditText = (EditText) findViewById(R.id.activity_register_name_editText);
        mEmailEditText = (EditText) findViewById(R.id.activity_register_email_editText);
        mAddressEditText = (EditText) findViewById(R.id.activity_register_address_editText);
        mPasswordEditText = (EditText) findViewById(R.id.activity_register_password_editText);
        mPhoneEditText = (EditText) findViewById(R.id.activity_register_phone_editText);

        mRegisterButton = (Button) findViewById(R.id.activity_register_register_button);
        mLoginButton = (Button) findViewById(R.id.activity_register_login_button);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        mDatabaseMain = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        mNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mEmailEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPasswordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmailEditText.getText().toString();
                password = mPasswordEditText.getText().toString();
                address = mAddressEditText.getText().toString();
                phone = mPhoneEditText.getText().toString();
                name = mNameEditText.getText().toString();


                if (mEmailEditText.getText().toString().length()<1)
                {
                    mEmailEditText.setError("Enter Email");
                }
                else if (mNameEditText.getText().toString().length()<1)
                {
                    mNameEditText.setError("Enter Name");
                }
                else if (mPhoneEditText.getText().toString().length()<1)
                {
                    mNameEditText.setError("Enter Phone");
                }
                else if (mAddressEditText.getText().toString().length()<1)
                {
                    mAddressEditText.setError("Enter Phone");
                }
                else if (mPasswordEditText.getText().toString().length()<1)
                {
                    mPasswordEditText.setError("Enter Password");
                }
                else if (!isValidEmailID(mEmailEditText.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    //  mEmailEditText.setError("Please Enter Valid Email");
                }
                else
                {
                    mProgressDialog = new ProgressDialog(RegisterActivity.this);
                    mProgressDialog.setTitle("Signing up...");
                    mProgressDialog.show();
                    mProgressDialog.setCancelable(false);
                    newEmailPassword();

                }

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }



    public void newEmailPassword() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    mProgressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Register Unsuccessful", Toast.LENGTH_SHORT).show();
                } else if (task.isSuccessful()) {
                    //write code for when register is successful
                    //after register successful this code adds new username


                    addUsername();



                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addUsername(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        User user = new User(email,userId,address,phone);
        mDatabaseMain.child("username").child(userId).setValue(user);

        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private boolean isValidEmailID(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }




}
