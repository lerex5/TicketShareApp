package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class buyerActivity extends AppCompatActivity {



    private String TAG = buyerActivity.class.getSimpleName();
    protected AutoCompleteTextView search;
    private RecyclerView.Adapter adapter;
    private ArrayList<TicketDetails> buylist;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;



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
        Button search1=findViewById(R.id.btnSearch);


        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(buyerActivity.this, android.R.layout.simple_dropdown_item_1line, tickets);
        search.setAdapter(searchAdapter);
        recyclerView = findViewById(R.id.rvAvailable);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        buylist=new ArrayList<>();
        adapter = new BuyerListView(buylist);
        recyclerView.setAdapter(adapter);

        SwiperClass swipeController = new SwiperClass();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAvailable();

            }
        });


    }

    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(this,ViewManager.class));
    }*/


    protected void GetAvailable(){

        String mov = search.getEditableText().toString();
        FirebaseDatabase.getInstance().goOnline();//InPersistentConnectionsOnly
        DatabaseReference avdb = FirebaseDatabase.getInstance().getReference(mov+"/Tickets");
        final DatabaseReference tick = FirebaseDatabase.getInstance().getReference("Tickets");
        buylist.clear();
        avdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final String a=postSnapshot.getKey();
                    DatabaseReference keydb = tick.child(Objects.requireNonNull(a));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDetails bList = dataSnapshot.getValue(TicketDetails.class);
                            if (bList != null) {
                                buylist.add(bList);
                                adapter.notifyDataSetChanged();
                            }
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
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
