package com.example.digiplantation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button ResetPasswordEmailbtn;
    private EditText eidtresetpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth=FirebaseAuth.getInstance();
        ResetPasswordEmailbtn=(Button) findViewById(R.id.sendreset);
        eidtresetpass=(EditText)findViewById(R.id.resetemail);
        ResetPasswordEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail= eidtresetpass.getText().toString();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(com.example.digiplantation.ResetPassword.this,"Please write your valid e-mail ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(com.example.digiplantation.ResetPassword.this,"Please check your Email Account, If you want to reset your password",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(com.example.digiplantation.ResetPassword.this, LoginActivity.class));
                            }
                            else
                            {
                                String message=task.getException().getMessage();
                                Toast.makeText(com.example.digiplantation.ResetPassword.this,"Error Occured!" + message,Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });

    }
}