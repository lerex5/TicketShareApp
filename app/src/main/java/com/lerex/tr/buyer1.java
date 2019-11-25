package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class buyer1 extends AppCompatActivity {

    private String TAG = buyerActivity.class.getSimpleName();
    protected AutoCompleteTextView search;
    private ArrayList<String> dateAl,costAl;
    private ArrayList<Integer> numAl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_buyer1);
        TinyDB tinydb = new TinyDB(this);//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        search=findViewById(R.id.tvSearch1);
        Button search1=findViewById(R.id.btnSearch1);

        dateAl=new ArrayList<>();
        costAl=new ArrayList<>();
        numAl=new ArrayList<>();

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(buyer1.this, android.R.layout.simple_dropdown_item_1line, tickets);
        search.setAdapter(searchAdapter);

        RecyclerView recyclerView=findViewById(R.id.rvAvailable1);
        BuyerListView adapter=new BuyerListView(dateAl,costAl,numAl,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAvailable();
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.BtmViewBar1);
        bottomNavigationView.setSelectedItemId(R.id.buyer);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.seller:
                        startActivity(new Intent(buyer1.this,ViewManBar.class));
                        return true;
                    case R.id.buyer:
                        return true;
                    case R.id.acct:
                        startActivity(new Intent(buyer1.this,accountActivity.class));
                }



                return false;
            }
        });
    }
    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    protected void GetAvailable(){

        String mov = search.getEditableText().toString();
        FirebaseDatabase.getInstance().goOnline();//InPersistentConnectionsOnly
        DatabaseReference avdb = FirebaseDatabase.getInstance().getReference(mov);
        final DatabaseReference tick = FirebaseDatabase.getInstance().getReference("Tickets");
        dateAl.clear();
        costAl.clear();
        numAl.clear();
        avdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final String a=postSnapshot.getKey();
                    DatabaseReference keydb = tick.child(Objects.requireNonNull(a));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDetails tDet=dataSnapshot.getValue(TicketDetails.class);
                            if(tDet != null) {

                                dateAl.add(tDet.getDate());
                                costAl.add(tDet.getCost());
                                numAl.add(tDet.getNumberOfTickets());
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
