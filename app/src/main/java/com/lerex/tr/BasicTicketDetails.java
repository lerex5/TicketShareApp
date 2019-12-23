package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BasicTicketDetails extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timesetlistener;
    private String date=null,time = null,name=null,select=null;
    private TinyDB tinydb;
    private TextView tDate,tTime;
    private AutoCompleteTextView tName;
    private AutoCompList adapter;
    private Button next;
    private RadioGroup tSelect;
    private RadioButton rb;
    private Integer checkedID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_basic_ticket_details);


        tinydb = new TinyDB(this);//Shared Preference To Get Localized Data
        ArrayList<String> tickets = tinydb.getListString("Movies");

        tName = findViewById(R.id.Name);
        tDate = findViewById(R.id.Date);
        tTime = findViewById(R.id.Time);
        tSelect = findViewById(R.id.select);
        next = findViewById(R.id.btnNext);

        adapter = new AutoCompList(BasicTicketDetails.this, android.R.layout.simple_dropdown_item_1line, tickets);
        tName.setAdapter(adapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedPreferences=getSharedPreferences("com.lerex.tr",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkSharedPreferences();

        tTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(BasicTicketDetails.this, R.style.DialogTheme, timesetlistener, hour, minute, false);
                timePickerDialog.show();
            }
        });

        timesetlistener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String hours, minutes;
                hours = Integer.toString(hour);
                minutes = Integer.toString(minute);
                if (hour < 10) {
                    hours = "0" + hours;
                }
                if (minute < 10) {
                    minutes = "0" + minutes;
                }
                time = hours + ":" + minutes;
                tTime.setText(time);
            }

        };
        tDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BasicTicketDetails.this, R.style.DialogTheme, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                date = day + "/" + month + "/" + year;
                tDate.setText(date);

            }
        };


        tSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                   checkedID = checkedId;
                                                   editor.putInt("select",checkedId);
                                                   switch (checkedId) {
                                                       case R.id.rbEvent:
                                                           select = "Event";
                                                           break;

                                                       case R.id.rbMovie:
                                                           select = "Movie";
                                                   }
                                               }
                                           }
        );


        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tName.getEditableText().toString();
                date=tDate.getText().toString();
                time=tTime.getText().toString();
                checkedID=tSelect.getCheckedRadioButtonId();
                switch (checkedID) {
                    case R.id.rbEvent:
                        select = "Event";
                        break;

                    case R.id.rbMovie:
                        select = "Movie";
                }
                editor.putString(getString(R.string.name),name);
                editor.putString(getString(R.string.date),date);
                editor.putString(getString(R.string.time),time);
                editor.commit();

                if (date == null || time == null || name.isEmpty() || select == null) {
                    Toast.makeText(BasicTicketDetails.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Date strDate = sdf.parse(date);
                        Date curDate = sdf.parse(sdf.format(new Date()));
                        if ((Objects.requireNonNull(curDate).before(strDate) || curDate.equals(strDate))) {
                            Intent intent = new Intent(BasicTicketDetails.this, CostDetails.class);
                            intent.putExtra("selection", select);
                            intent.putExtra("name", name);
                            intent.putExtra("date", date);
                            intent.putExtra("time", time);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BasicTicketDetails.this, "Date Invalid", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private void checkSharedPreferences(){
        String n=sharedPreferences.getString(getString(R.string.name),"");
        String d=sharedPreferences.getString(getString(R.string.date),"");
        String t=sharedPreferences.getString(getString(R.string.time),"");
        int s=sharedPreferences.getInt("select",0);
        if (s != 0) {
            rb = findViewById(s);
            rb.setChecked(true);
        }
        tName.setText(n);
        tDate.setText(d);
        tTime.setText(t);
    }
}
