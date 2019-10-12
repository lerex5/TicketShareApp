package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password,retypepassword;

    protected void RegisterUser(){

        String semail = email.getText().toString();
        String spassword = password.getText().toString();
        String srpassword = retypepassword.getText().toString();

        if (TextUtils.isEmpty(semail)){
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(spassword)){
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(srpassword)){
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!spassword.equals(srpassword)){
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(semail, spassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            if (task.isSuccessful()) {
                                //User is successfully registered and logged in
                                //start Profile Activity here
                                Toast.makeText(SignupActivity.this, "registration successful",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }else{

                                Toast.makeText(SignupActivity.this, "Couldn't register, try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        Button signupbtn = findViewById(R.id.btnSignup);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.etEmail);
                password=findViewById(R.id.etPass);
                retypepassword=findViewById(R.id.etRepass);
                RegisterUser();
            }
        });

    }
}
