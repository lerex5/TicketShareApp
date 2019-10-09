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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

class event {
    public String eventName,cost,sellerId;
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
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");//RealTime Database Connection
    protected void addTickets(){
        EditText eName = findViewById(R.id.etName);
        EditText eDate = findViewById(R.id.etDate);
        EditText eCost = findViewById(R.id.etCost);
        EditText eNo = findViewById(R.id.etNumber);
        event newEvent = new event(eName.getText().toString(),eCost.getText().toString(),(Date) eDate.getText(),Integer.valueOf(eNo.getText().toString()));

    }
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
                aPop.setFocusable(true);
                aPop.update();
                //display the popup window
                aPop.showAtLocation(sellerLayout, Gravity.CENTER, 0, 0);

                Button Add1=customView.findViewById(R.id.btnAdd1);
                Add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTickets();
                        aPop.dismiss();
                    }
                });
            }
        });

    }
}
