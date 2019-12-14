package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
public class sellActivity extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TicketDetails> selList;

    private TextView curCity;
    private TinyDB tinydb;

    private Button Add;
    private String TAG = sellActivity.class.getSimpleName();
    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.activity_sell,null);
        tinydb = new TinyDB(getActivity());
        while (mAuth.getCurrentUser()== null){}
        ref= database.getReference(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        Add=view.findViewById(R.id.btnAdd);

        curCity=view.findViewById(R.id.textCurrentLocation);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        selList=new ArrayList<>();
        mAdapter = new SellerListView(selList);
        recyclerView.setAdapter(mAdapter);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),addTickets.class);
                startActivity(intent);

            }
        });

        curCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LocationActivity.class));
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //curCity.setText("Currently Selected City is "+tinydb.getString("CurCity")+"  Click Here To Change");
        View viewNavigation=getActivity().findViewById(R.id.bottomNavigationView);
        if(viewNavigation instanceof BottomNavigationView){
            BottomNavigationView bottomNavView=(BottomNavigationView)viewNavigation;
            bottomNavView.setSelectedItemId(R.id.seller);
        }
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
                    final String unikey=postSnapshot.getKey();
                    final DatabaseReference keydb = tick.child(Objects.requireNonNull(unikey));
                    keydb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TicketDetails sList=dataSnapshot.getValue(TicketDetails.class);
                            if(sList != null&&sList.getTransactionMode()==0
                            ) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = null,curDate = null;
                                try {
                                    strDate = sdf.parse(sList.getDate());
                                    curDate = sdf.parse(sdf.format(new Date()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (curDate.after(strDate)) {
                                    tick.child(sList.getFirebaseId()).child("transactionMode").setValue(4);//Inactive Tickets;
                                }
                                else {
                                    selList.add(sList);
                                    mAdapter.notifyDataSetChanged();
                                }
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
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        ref.addValueEventListener(movielistListener);

    }
}
/* <TextView
        android:id="@+id/tealView"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:background="#5BE2EA"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />*/