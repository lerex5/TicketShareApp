package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import com.lerex.tr.TinyDB;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewManager extends AppCompatActivity {

    private String TAG = ViewManager.class.getSimpleName();
    private ArrayList<String> movies=new ArrayList<>();
    private TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_view_manager);




        Button sell=findViewById(R.id.btnSell);
        Button buy=findViewById(R.id.btnBuy);
        Button bar=findViewById(R.id.btnVM);


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewManager.this,sellActivity.class));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewManager.this,buyerActivity.class));
            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewManager.this,FragHome.class));
            }
        });

    }
    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        finish();
    }



}
