package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lerex.tr.TinyDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class addTickets extends AppCompatActivity {


    private String TAG = addTickets.class.getSimpleName();
    private ArrayList<String> Tickets;
    private ArrayAdapter<String> TickAdapter;

    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");
    //private DatabaseReference availabledb = FirebaseDatabase.getInstance().getReference("Available");


    protected void addTicket(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        AutoCompleteTextView eName = findViewById(R.id.etRef1);
        EditText eDate = findViewById(R.id.etDate);
        EditText eCost = findViewById(R.id.etCost);
        EditText eNo = findViewById(R.id.etNumber);
        EditText eCity = findViewById(R.id.etCity);
        EditText eTheatre = findViewById(R.id.etTheatre);
        TicketDetails newEvent = new TicketDetails(eName.getText().toString(),eCost.getText().toString(),eDate.getText().toString(),Integer.valueOf(eNo.getText().toString()),curuser,eCity.getText().toString(),eTheatre.getText().toString());

        DatabaseReference moviedb = FirebaseDatabase.getInstance().getReference(eName.getText().toString()+"/"+"Tickets");
        DatabaseReference userdb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        DatabaseReference citydb = FirebaseDatabase.getInstance().getReference(eName.getText().toString()+"/"+eCity.getText().toString()+"/"+eTheatre.getText().toString());

        mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
        moviedb.child(key).setValue("");
        userdb.child(key).setValue("");
        citydb.child(key).setValue("");

       // availabledb.child(key).setValue("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_add_tickets);

        TinyDB tinydb = new TinyDB(this);//Shared Preference To Get Localized Data
        Tickets=tinydb.getListString("Movies");

        Button add1 = findViewById(R.id.btnAdd1);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTicket();
                //Intent intent=new Intent(addTickets.this,sellActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        AutoCompleteTextView act =findViewById(R.id.etRef1);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(addTickets.this,android.R.layout.simple_dropdown_item_1line,Tickets);
        act.setAdapter(adapter);
    }

}
