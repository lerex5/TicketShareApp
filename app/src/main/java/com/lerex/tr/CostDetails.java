package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CostDetails extends AppCompatActivity {

    private EditText tVenue,tCost;
    private Spinner tNum;
    private Button next,back;
    private String Venue=null,Cost=null,Name,Date,Time,Select;
    private Integer Num=1;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected void addTicket(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        TinyDB tinydb = new TinyDB(this);
        String city= tinydb.getString("CurCity");//eCity.getText().toString();

        TicketDetails newEvent = new TicketDetails(Name,Cost,Date,Num,curuser,city,Venue,key,Time);

        DatabaseReference moviedb = FirebaseDatabase.getInstance().getReference(Name + "/" + "Tickets");
        DatabaseReference userdb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        DatabaseReference citydb = FirebaseDatabase.getInstance().getReference(Name+ "/" + city + "/" + Venue);

        mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
        moviedb.child(key).setValue("");
        userdb.child(key).setValue("");
        citydb.child(key).setValue("");
        startActivity(new Intent(CostDetails.this,FragHome.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_cost_details);


        tVenue=findViewById(R.id.Venue);
        tCost=findViewById(R.id.Cost);
        tNum=findViewById(R.id.Number);
        next=findViewById(R.id.btnNext1);
        back=findViewById(R.id.btnBack1);

        final List<Integer> noOfTickets=new ArrayList<>();

        noOfTickets.add(1);
        noOfTickets.add(2);
        noOfTickets.add(3);
        noOfTickets.add(4);
        noOfTickets.add(5);

        ArrayAdapter<Integer> NumAdapter=new ArrayAdapter<>(this,R.layout.spinner_view,noOfTickets);
        NumAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tNum.setAdapter(NumAdapter);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        checkSharedPreferences();


        tNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Num=Integer.valueOf(noOfTickets.get(position).toString());
                editor.putInt("num",position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Venue=tVenue.getText().toString();
                Cost=tCost.getText().toString();
                if(Venue.isEmpty()||Cost.isEmpty()) {
                    Toast.makeText(CostDetails.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=getIntent();
                    Select=intent.getStringExtra("select");
                    Name=intent.getStringExtra("name");
                    Date=intent.getStringExtra("date");
                    Time=intent.getStringExtra("time");
                    addTicket();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(getString(R.string.venue),tVenue.getText().toString());
                editor.putString(getString(R.string.cost),tCost.getText().toString());
                editor.commit();
                startActivity(new Intent(CostDetails.this,BasicTicketDetails.class));
            }
        });

    }
    private void checkSharedPreferences(){
        String v=sharedPreferences.getString(getString(R.string.venue),"");
        String c=sharedPreferences.getString(getString(R.string.cost),"");
        int n= sharedPreferences.getInt("num", 0);
        tVenue.setText(v);
        tCost.setText(c);
        tNum.setSelection(n);
    }
}
