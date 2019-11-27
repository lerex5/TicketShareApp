package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class buyer1 extends Fragment{

    protected AutoCompleteTextView search;
    private ArrayList<TicketDetails> buylist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView tv;
    private Button search1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_buyer1, null);
        TinyDB tinydb = new TinyDB(getActivity());//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        search=view.findViewById(R.id.tvSearch1);
        search1=view.findViewById(R.id.btnSearch1);
        tv=view.findViewById(R.id.tv);


        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, tickets);
        search.setAdapter(searchAdapter);
        recyclerView = view.findViewById(R.id.rvAvailable1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        search1=view.findViewById(R.id.btnSearch1);
        recyclerView = view.findViewById(R.id.rvAvailable1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        buylist=new ArrayList<>();
        adapter = new BuyerListView(buylist);
        recyclerView.setAdapter(adapter);

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAvailable();
                Toast.makeText(getActivity(),"HI",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }

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


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(buyer1.this).attach(buyer1.this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        View view1=getActivity().findViewById(R.id.bottomNavigationView);
        if(view1 instanceof BottomNavigationView){
            BottomNavigationView bottomNavView=(BottomNavigationView)view1;
            bottomNavView.setSelectedItemId(R.id.buyer);
        }
    }
}

