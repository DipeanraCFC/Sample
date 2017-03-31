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

    EditText mNameEditText, mEmailEditText, mPasswordEditText, mAddressEditText, mPhoneEditText, mConfirmPasswordEditText;
    Button mRegisterButton, mLoginButton;
    ProgressDialog mProgressDialog;
    String email, password;
    Uri file;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseMain;
    private StorageReference mStorageRef;
    private StorageReference riversRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mNameEditText = (EditText) findViewById(R.id.activity_register_name_editText);
        mEmailEditText = (EditText) findViewById(R.id.activity_register_email_editText);
        mAddressEditText = (EditText) findViewById(R.id.activity_register_address_editText);
        mPasswordEditText = (EditText) findViewById(R.id.activity_register_password_editText);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.activity_register_confirmpassword_editText);
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
                email = mEmailEditText.getText().toString().trim();
                password = mPasswordEditText.getText().toString().trim();


                if (mNameEditText.getText().toString().length() < 1) {
                    mNameEditText.setError("Please enter name");
                } else if (mEmailEditText.getText().toString().length() < 1) {
                    mEmailEditText.setError("Please enter email");
                } else if (mPasswordEditText.getText().toString().length() < 1) {
                    mPasswordEditText.setError("Please enter password");
                } else if (!mConfirmPasswordEditText.getText().toString().equals(mPasswordEditText.getText().toString()))

                {
                    mConfirmPasswordEditText.setError("Password  not matched");

                } else if (mAddressEditText.getText().toString().length() < 1) {



                } else if (mPasswordEditText.getText().toString().length() < 6) {
                    mAddressEditText.setError("Password must contain 6 characters");
                } else if (mPhoneEditText.getText().toString().length() < 1) {
                    mPhoneEditText.setError("Please enter Phone");
                } else if (!isValidEmailID(mEmailEditText.getText().toString())) {
                    // Toast.makeText(RegisterActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                    mEmailEditText.setError("Please enter valid email");
                } else {
                    mProgressDialog = new ProgressDialog(RegisterActivity.this);
                    mProgressDialog.setTitle("Signing in");
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

    public void addUsername() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = mEmailEditText.getText().toString().trim();

        String name = mNameEditText.getText().toString().trim();
        String address = mAddressEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();

        User user = new User(email, address, phone, name);
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
