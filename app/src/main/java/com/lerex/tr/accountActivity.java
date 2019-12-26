package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class accountActivity extends Fragment {

    private Button LogOut,chngLocation,YourTickets;
    private TextView curCity,LocSpace,TicSpace,UserId,SoldCnt;
    private TinyDB tinydb;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref,sold;
    private long count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, null);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ref= database.getReference(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        sold =FirebaseDatabase.getInstance().getReference("Sold");

        tinydb = new TinyDB(getActivity());
        chngLocation=view.findViewById(R.id.chngLocation);
        curCity=view.findViewById(R.id.curCity);
        LocSpace=view.findViewById(R.id.LocSpace);
        TicSpace=view.findViewById(R.id.TicSpace);
        UserId=view.findViewById(R.id.UserId);
        SoldCnt=view.findViewById(R.id.SoldCnt);

        LogOut = view.findViewById(R.id.btnLogout);
        YourTickets = view.findViewById(R.id.viewTickets);

        curCity.setText(tinydb.getString("CurCity"));
        UserId.setText(currentUser.getPhoneNumber());
        sold.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count=dataSnapshot.getChildrenCount();
                SoldCnt.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        TicSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), YourTickets.class));
            }
        });
        YourTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), YourTickets.class));
            }
        });

        LocSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LocationActivity.class));
            }
        });
        chngLocation.setOnClickListener(new View.OnClickListener() {
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
        curCity.setText(tinydb.getString("CurCity"));


        View view1=getActivity().findViewById(R.id.bottomNavigationView);
        if(view1 instanceof BottomNavigationView){
            BottomNavigationView bottomNavView=(BottomNavigationView)view1;
            bottomNavView.setSelectedItemId(R.id.acct);
        }
    }

}
//#33aab8c2