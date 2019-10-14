package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

class event {
    public String eventName,cost,sellerId,buyerId,eventDate;
    public int noOfTickets;

    public  event(String eventName,String cost,String eventDate,int noOfTickets,String sellerId){
        this.eventDate=eventDate;
        this.eventName=eventName;
        this.cost=cost;
        this.noOfTickets=noOfTickets;
        this.sellerId=sellerId;
    }
}

public class sellActivity extends AppCompatActivity {

    private event newEvent;
    private View customView;
    private PopupWindow aPop;
    private ConstraintLayout sellerLayout;
    private Button Add,Add1;
    private ListView lv;
    private ArrayList<String> Tickets;
    private ArrayAdapter<String> TickAdapter;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");
    private DatabaseReference availabledb = FirebaseDatabase.getInstance().getReference("Available");
    private DatabaseReference userdb,moviedb;

    protected void addTickets(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        EditText eName = customView.findViewById(R.id.etName);
        EditText eDate = customView.findViewById(R.id.etDate);
        EditText eCost = customView.findViewById(R.id.etCost);
        EditText eNo = customView.findViewById(R.id.etNumber);
        newEvent = new event(eName.getText().toString(),eCost.getText().toString(),eDate.getText().toString(),Integer.valueOf(eNo.getText().toString()),curuser);

        moviedb=FirebaseDatabase.getInstance().getReference(eName.getText().toString());
        userdb= FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
        moviedb.child(key).setValue("");
        userdb.child(key).setValue("");
        availabledb.child(key).setValue("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sell);

        sellerLayout=findViewById(R.id.sellerLa);

        Add=findViewById(R.id.btnAdd);
        lv=findViewById(R.id.lvTickets);
        Tickets=new ArrayList<>();
        TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) sellActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                customView = Objects.requireNonNull(layoutInflater).inflate(R.layout.addticket,null);

                //instantiate popup window
                aPop = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                aPop.setFocusable(true);
                aPop.update();
                aPop.showAtLocation(sellerLayout, Gravity.CENTER, 0, 0);

                Add1=customView.findViewById(R.id.btnAdd1);
                Add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTickets();
                        aPop.dismiss();//Dismiss pop up
                        Tickets.add(newEvent.eventName);
                        TickAdapter.notifyDataSetChanged();
                        lv.setAdapter(TickAdapter);

                    }
                });


            }
        });



    }
}
