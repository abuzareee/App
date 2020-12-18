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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText mName,mMail,mPassword,mPhone;
    Button mRegisterBtn;
    CheckBox i1,i2,i3;
    FirebaseAuth fAuth;
    FirebaseDatabase RootNode;
    DatabaseReference rf;
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName=findViewById(R.id.username);
        mMail=findViewById(R.id.etEmail);
        i1=findViewById(R.id.userRegCheckbox);
        i2=findViewById(R.id.gardenerRegCheckbox);
        i3=findViewById(R.id.adminRegCheckbox);
        mPassword=findViewById(R.id.etPassword);
        mPhone=findViewById(R.id.phone);
        mRegisterBtn=findViewById(R.id.signUp);
        fAuth=FirebaseAuth.getInstance();
      //  if(fAuth.getCurrentUser() !=null ) {
       //     startActivity(new Intent(getApplicationContext(), HomeUser.class));
        //    finish();
       // }
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
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootNode=FirebaseDatabase.getInstance();
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
                //Register User in Firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fuser=fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(com.example.digiplantation.RegisterActivity.this,"Verification Email Has Been Sent.",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not Sent." + e.getMessage());
                                }
                            });
                            //Get All Values
                            String name=mName.getEditableText().toString();
                            String email=mMail.getEditableText().toString();
                            String password=mPassword.getEditableText().toString();
                            String phone=mPhone.getEditableText().toString();
                            if(i1.isChecked()) {
                                rf=RootNode.getReference("Users");
                                UserHelperClass helperClass=new UserHelperClass();
                                rf.child(phone).setValue(helperClass);
                                Toast.makeText(com.example.digiplantation.RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                            else if(i2.isChecked()){
                                rf=RootNode.getReference("Gardeners");
                                UserHelperClass helperClass=new UserHelperClass();
                                rf.child(phone).setValue(helperClass);
                                Toast.makeText(com.example.digiplantation.RegisterActivity.this, "Gardener Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                            else if (i3.isChecked()){
                                Toast.makeText(com.example.digiplantation.RegisterActivity.this, "Admin Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                            else{
                                Toast.makeText(com.example.digiplantation.RegisterActivity.this,"Nothing is checked"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(com.example.digiplantation.RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}