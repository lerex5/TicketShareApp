package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {

    private TextView curCity;
    private TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application


        setContentView(R.layout.activity_location);

        curCity = findViewById(R.id.textCurrentLocation);
        tinydb = new TinyDB(this);//Shared Preference To Get Localized Data

        final ArrayList<String> Cities = tinydb.getListString("Cities");
        final AutoCompleteTextView CityDropDown =findViewById(R.id.etCity);
        final AutoCompList CityAdapter=new AutoCompList(LocationActivity.this,android.R.layout.simple_dropdown_item_1line, Cities);
        CityDropDown.setAdapter(CityAdapter);

        final Button continueBtn = findViewById(R.id.btnLocation);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CityDropDown.getEditableText().toString().isEmpty()){
                    Toast.makeText(v.getContext(), "Select A City",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!Cities.contains(CityDropDown.getEditableText().toString())){
                    Toast.makeText(v.getContext(), "Enter A Valid City Name",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    tinydb.putString("CurCity",CityDropDown.getEditableText().toString());
                    startActivity(new Intent(LocationActivity.this, FragHome.class));
                }
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if(!tinydb.getString("CurCity").isEmpty()){
            curCity.setText("You Are Currently In "+tinydb.getString("CurCity"));
        }
    }
}
