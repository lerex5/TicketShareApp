package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lerex.tr.TinyDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String date;
    private TextView eDate;
    private Spinner eNo;
    private int num;


    protected void addTicket(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        AutoCompleteTextView eName = findViewById(R.id.etRef1);
        EditText eCost = findViewById(R.id.etCost);
        EditText eCity = findViewById(R.id.etCity);
        EditText eTheatre = findViewById(R.id.etTheatre);

        TicketDetails newEvent = new TicketDetails(eName.getText().toString(),eCost.getText().toString(),date,num,curuser,eCity.getText().toString(),eTheatre.getText().toString());

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

        eDate = findViewById(R.id.etDate);

        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        addTickets.this,R.style.DialogTheme,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });


        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                date=day+"/"+month+"/"+year;
                eDate.setText(date);

            }
        };

        eNo = findViewById(R.id.etNumber);

        final List<Integer> noOfTickets=new ArrayList<>();

        noOfTickets.add(1);
        noOfTickets.add(2);
        noOfTickets.add(3);
        noOfTickets.add(4);
        noOfTickets.add(5);

        ArrayAdapter<Integer> NumAdapter=new ArrayAdapter<>(this,R.layout.spinner_view,noOfTickets);
        NumAdapter.setDropDownViewResource(R.layout.spinner_view);
        eNo.setAdapter(NumAdapter);

        eNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num=Integer.valueOf(noOfTickets.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
