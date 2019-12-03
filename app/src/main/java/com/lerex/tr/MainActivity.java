package com.lerex.tr;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    protected void verifyNumber(){
        String ph=phoneNumber.getText().toString().trim();
        Intent intent = new Intent(getBaseContext(), SignupActivity.class);
        intent.putExtra("PHONE_NUMBER", ph);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String userid = currentUser.getUid();
            finish();
            startActivity(new Intent(getApplicationContext(),
                    FragHome.class));
        }
        else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

            setContentView(R.layout.activity_main);

            phoneNumber= findViewById(R.id.phoneNo);
            Button logIn = findViewById(R.id.login);
            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyNumber();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}
