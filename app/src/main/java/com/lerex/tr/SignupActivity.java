package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    String pas,repas;

    //EditText email=findViewById(R.id.etEmail);
    EditText pass=findViewById(R.id.etPass);
    EditText repass=findViewById(R.id.etRepass);
    Button sign=findViewById(R.id.btnSignup);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pas=pass.getText().toString();
                repas=repass.getText().toString();
                if(!pas.equals(repas))
                {
                    repass.setError("Password doesn't match");
                }
            }
        });
    }
}
