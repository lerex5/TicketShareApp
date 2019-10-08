package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
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


    private PopupWindow aPop;
    private ConstraintLayout sellerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sell);

        sellerLayout=findViewById(R.id.sellerLa);
        Button Add=findViewById(R.id.btnAdd);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) sellActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.addticket,null);

                //instantiate popup window
                aPop = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                //display the popup window
                aPop.showAtLocation(sellerLayout, Gravity.CENTER, 0, 0);
            }
        });
    }
}
