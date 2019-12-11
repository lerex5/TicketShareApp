package com.lerex.tr;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class YourTickets extends AppCompatActivity {

    private EditText phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TicketDetails> TicList;

    private String TAG = YourTickets.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application
        setContentView(R.layout.activity_your_tickets);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ref= database.getReference(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TicList=new ArrayList<>();
        mAdapter = new TicketListView(TicList);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStart()
    {
        super.onStart();

        ValueEventListener movielistListener = new ValueEventListener() {
            final DatabaseReference tick = database.getReference("Tickets");

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TicList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final String unikey=postSnapshot.getKey();
                    final DatabaseReference keydb = tick.child(Objects.requireNonNull(unikey));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDetails sList=dataSnapshot.getValue(TicketDetails.class);
                            if(sList != null&&sList.getTransactionMode()!=0) {
                                TicList.add(sList);
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
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(movielistListener);

    }


}