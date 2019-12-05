package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseUser curuser;
    private EditText otp;
    private Button verifybtn;
    private TextView resendbtn;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String VerificationId,phoneNumber;
    private TinyDB tinydb;

    protected void SignIn(@NonNull PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            curuser = Objects.requireNonNull(task.getResult()).getUser();
                        }
                        else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(SignupActivity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if(!tinydb.getString("CurCity").isEmpty()){
        startActivity(new Intent(SignupActivity.this,FragHome.class));
        }
        else {
            startActivity(new Intent(SignupActivity.this,LocationActivity.class));
        }
        finish();
    }

    protected void registerUser() {
        String sotp = otp.getText().toString();
        if (TextUtils.isEmpty(sotp)) {
            Toast.makeText(this, "A Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, sotp);
        SignIn(credential);
    }

    protected void resendOtp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,30,TimeUnit.SECONDS,this,
                mCallbacks,mResendToken);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tinydb=new TinyDB(this);//Shared Preference To Get Localized Data
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        initFireBaseCallbacks();

        phoneNumber = "+91"+getIntent().getStringExtra("PHONE_NUMBER").trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60,TimeUnit.SECONDS,this, mCallbacks);

        verifybtn=findViewById(R.id.Verify);
        resendbtn=findViewById(R.id.ResendOtp);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = findViewById(R.id.etOtp);
                registerUser();
            }
        });

        resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
    }

    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                SignIn(credential);
                finish();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SignupActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(SignupActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                VerificationId = verificationId;
                mResendToken = token;
            }

        };
    }

}