package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewManBar extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TicketDetails> selList;

    private Button Add;
    private String TAG = sellActivity.class.getSimpleName();
    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_man_bar);


        ref= database.getReference(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        Add=findViewById(R.id.btnAdd1);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        selList=new ArrayList<>();
        mAdapter = new SellerListView(selList);
        recyclerView.setAdapter(mAdapter);

        SwiperClass swipeController = new SwiperClass();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewManBar.this,addTickets.class);
                startActivity(intent);

            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.BtmViewBar);
        bottomNavigationView.setSelectedItemId(R.id.seller);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.seller:
                        return true;
                    case R.id.buyer:
                        startActivity(new Intent(ViewManBar.this,buyer1.class));
                        return true;
                    case R.id.acct:
                         startActivity(new Intent(ViewManBar.this,accountActivity.class));
                         return true;
                }



                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener movielistListener = new ValueEventListener() {
            final DatabaseReference tick = database.getReference("Tickets");

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String a=postSnapshot.getKey();
                    DatabaseReference keydb = tick.child(Objects.requireNonNull(a));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDetails sList=dataSnapshot.getValue(TicketDetails.class);
                            if(sList != null) {
                                selList.add(sList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ViewManBar.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        ref.addValueEventListener(movielistListener);

    }
}
