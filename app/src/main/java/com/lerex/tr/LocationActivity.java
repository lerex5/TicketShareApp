package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private TextView curCity;
    private TinyDB tinydb;
    private String CityName;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        curCity = findViewById(R.id.textCurrentLocation);
        tinydb = new TinyDB(this);//Shared Preference To Get Localized Data

        final ArrayList<String> Cities = tinydb.getListString("Cities");
        final AutoCompleteTextView CityDropDown = findViewById(R.id.etCity);
        final AutoCompList CityAdapter = new AutoCompList(LocationActivity.this, android.R.layout.simple_dropdown_item_1line, Cities);
        CityDropDown.setAdapter(CityAdapter);

        final Button continueBtn = findViewById(R.id.btnLocation);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CityDropDown.getEditableText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Select A City",
                            Toast.LENGTH_SHORT).show();
                } else if (!Cities.contains(CityDropDown.getEditableText().toString())) {
                    Toast.makeText(v.getContext(), "Enter A Valid City Name",
                            Toast.LENGTH_SHORT).show();
                } else {
                    tinydb.putString("CurCity", CityDropDown.getEditableText().toString());
                    startActivity(new Intent(LocationActivity.this, FragHome.class));
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        if(!tinydb.getString("CurCity").isEmpty()){
            curCity.setText("You Are Currently In "+tinydb.getString("CurCity"));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                CityName = addresses.get(0).getLocality();
                Toast.makeText(this,CityName,
                        Toast.LENGTH_SHORT).show();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
