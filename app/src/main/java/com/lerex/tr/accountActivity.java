package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class accountActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_account);


        BottomNavigationView bottomNavigationView = findViewById(R.id.BtmViewBar2);
        bottomNavigationView.setSelectedItemId(R.id.acct);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.seller:
                        startActivity(new Intent(accountActivity.this, ViewManBar.class));
                        return true;
                    case R.id.buyer:
                        startActivity(new Intent(accountActivity.this, buyer1.class));
                        return true;
                    case R.id.acct:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
