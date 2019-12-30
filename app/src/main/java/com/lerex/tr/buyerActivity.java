package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import static androidx.core.content.ContextCompat.getSystemService;

public class buyerActivity extends Fragment{

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected AutoCompleteTextView search;
    private ArrayList<TicketDetails> buylist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView chngCurCity,curCity;
    private Button searchbtn,clear,clearLogo,searchLogo;
    private TinyDB tinydb;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_buyer, null);

        tinydb = new TinyDB(getActivity());//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        search=view.findViewById(R.id.tvSearch);
        searchbtn=view.findViewById(R.id.btnSearch);
        searchLogo=view.findViewById(R.id.SearchLogo);
        chngCurCity=view.findViewById(R.id.textCurrentLocation);
        curCity=view.findViewById(R.id.currentCity);
        clear=view.findViewById(R.id.btnClear);
        clearLogo=view.findViewById(R.id.ClearLogo);

        final AutoCompList searchAdapter = new AutoCompList(Objects.requireNonNull(getActivity()), android.R.layout.simple_dropdown_item_1line, tickets);
        search.setAdapter(searchAdapter);

        recyclerView = view.findViewById(R.id.rvAvailable);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        buylist=new ArrayList<>();
        adapter = new BuyerListView(buylist);
        recyclerView.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setVisibility(View.VISIBLE);
                clearLogo.setVisibility(View.VISIBLE);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search.setText("");
                    }
                });

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(),0);
                clear.setVisibility(View.INVISIBLE);
                clearLogo.setVisibility(View.INVISIBLE);
                if(search.getEditableText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter A Movie Name",Toast.LENGTH_LONG).show();
                }
                else {
                    GetAvailable();
                }
            }
        });
        searchLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(),0);
                clear.setVisibility(View.INVISIBLE);
                clearLogo.setVisibility(View.INVISIBLE);
                if(search.getEditableText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter A Movie Name",Toast.LENGTH_LONG).show();
                }
                else {
                    GetAvailable();
                }
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
                            if (bList != null&&bList.getTransactionMode()==0&&bList.getCity().equals(tinydb.getString("CurCity"))&&!bList.getSellerId().equals(mAuth.getCurrentUser().getPhoneNumber())) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = null,curDate = null;
                                try {
                                    strDate = sdf.parse(bList.getDate());
                                    curDate = sdf.parse(sdf.format(new Date()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (curDate.after(strDate)) {
                                    tick.child(bList.getFirebaseId()).child("transactionMode").setValue(4);//Inactive Tickets;
                                }
                                else {
                                    buylist.add(bList);
                                    adapter.notifyDataSetChanged();
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //Is this Necessary And If So Why is It

        /*assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(buyerActivity.this).attach(buyerActivity.this).commit();*/
        if(buylist==null)
            Toast.makeText(getActivity(),"No Tickets available",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //chngCurCity.setText("You Are Viewing Tickets For "+tinydb.getString("curCity")+"  Click Here To Change");
        curCity.setText(tinydb.getString("CurCity"));

        View viewNavigation= Objects.requireNonNull(getActivity()).findViewById(R.id.bottomNavigationView);
        if(viewNavigation instanceof BottomNavigationView){
            BottomNavigationView bottomNavView=(BottomNavigationView)viewNavigation;
            bottomNavView.setSelectedItemId(R.id.buyer);
        }
    }
}

