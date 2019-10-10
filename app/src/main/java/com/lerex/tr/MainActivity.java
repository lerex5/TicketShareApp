package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;

/*
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lerex.tr.R;
*/

public class MainActivity extends AppCompatActivity {

//    String e_id,pas;

    TextView sign=findViewById(R.id.tvSignup);
 //   EditText email_id=findViewById(R.id.email);
   // EditText pass=findViewById(R.id.password);
    Button login=findViewById(R.id.login);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_main);


        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //pass.setError("Password doesn't match!");
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });*/
    }
}