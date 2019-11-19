package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;


public class buyerActivity extends AppCompatActivity {



    private String TAG = buyerActivity.class.getSimpleName();
    private ArrayList<TicketDetails> ticketDetails;
    private ListView available;
    private MyListView adapter;
    protected AutoCompleteTextView search;






    private static class TicketDet{
        public String eventName,eventDate,cost,sellerId;
        public int noOfTickets;

        public TicketDet(){}
        public TicketDet(String cost,String eventDate,String eventName,int noOfTickets,String sellerId){
            this.cost=cost;
            this.eventDate=eventDate;
            this.eventName=eventName;
            this.noOfTickets=noOfTickets;
            this.sellerId=sellerId;
        }

        public String geteventName(){ return eventName; }
        public String geteventDate(){
            return eventDate;
        }
        public String getcost(){
            return cost;
        }
        public int getNoOfTickets(){
            return noOfTickets;
        }
        public String getSellerId(){
            return sellerId;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_buyer);



        TinyDB tinydb = new TinyDB(this);//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        search=findViewById(R.id.tvSearch);
        available = findViewById(R.id.lvAvailable);
        Button search1=findViewById(R.id.btnSearch);
        ticketDetails=new ArrayList<>();
        adapter=new MyListView(this,R.layout.activity_listview,ticketDetails);
        available.setAdapter(adapter);

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(buyerActivity.this, android.R.layout.simple_dropdown_item_1line, tickets);
        search.setAdapter(searchAdapter);



        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketDetails.clear();
                GetAvailable();
                adapter.notifyDataSetChanged();

            }
        });





    }
    protected void GetAvailable(){

        String mov = search.getEditableText().toString();
        FirebaseDatabase.getInstance().goOnline();
        DatabaseReference avdb = FirebaseDatabase.getInstance().getReference(mov);

        avdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String a=postSnapshot.getKey();
                    DatabaseReference keydb = FirebaseDatabase.getInstance().getReference("Tickets").child(Objects.requireNonNull(a));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDet tDet=dataSnapshot.getValue(TicketDet.class);
                            assert tDet != null;
                            ticketDetails.add(new TicketDetails(tDet.getNoOfTickets(),tDet.geteventDate(),tDet.getcost()));


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
