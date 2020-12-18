package com.example.digiplantation;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText mMail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,mForgot;
    CheckBox i1,i2,i3;
    FirebaseAuth fAuth;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        i1=findViewById(R.id.userLoginCheckbox);
        i2=findViewById(R.id.gardenerLoginCheckbox);
        i3=findViewById(R.id.adminLoginCheckbox);
        mMail=findViewById(R.id.etEmail);
        mPassword=findViewById(R.id.etPassword);
        mLoginBtn=findViewById(R.id.loginButtonLoginScreen);
        mCreateBtn=findViewById(R.id.skipLogin);
        mForgot=findViewById(R.id.bReset);
        fAuth=FirebaseAuth.getInstance();
        i1.setChecked(true);
        i1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(i1.isChecked()){
                    i2.setChecked(false);
                    i3.setChecked(false);
                }
            }
        });
        i2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(i2.isChecked()){
                    i1.setChecked(false);
                    i3.setChecked(false);
                }
            }
        });
        i3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(i3.isChecked()){
                    i1.setChecked(false);
                    i2.setChecked(false);
                }
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mMail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mMail.setError("Email is Required. ");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required. ");
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must >= 6 Characters. ");
                }
                //Authenticate User
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(i1.isChecked()) {
                                if(fAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(com.example.digiplantation.LoginActivity.this, "User Logged In Successfully ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), com.example.digiplantation.HomeUser.class));
                                }
                            }
                            else if(i2.isChecked()){
                                if(fAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(LoginActivity.this, "Gardener Logged In Successfully ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), com.example.digiplantation.HomeGardener.class));
                                }
                            }
                            else if(i3.isChecked()){
                                if(fAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(com.example.digiplantation.LoginActivity.this, "Admin Logged In Successfully ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), com.example.digiplantation.HomeAdmin.class));
                                }
                            }
                            else{
                                Toast.makeText(com.example.digiplantation.LoginActivity.this,"Nothing is checked"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(com.example.digiplantation.LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Not Verified. Please verify first" + e.getMessage());
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}