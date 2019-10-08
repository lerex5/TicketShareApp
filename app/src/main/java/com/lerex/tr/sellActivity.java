package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Date;

class event {
    public String eventName,cost;
    public Date eventDate;
    public int noOfTickets;

    public  event(String eventName,String cost,Date eventDate,int noOfTickets){
        this.eventDate=eventDate;
        this.eventName=eventName;
        this.cost=cost;
        this.noOfTickets=noOfTickets;
    }
}

public class sellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sell);


    }
}
