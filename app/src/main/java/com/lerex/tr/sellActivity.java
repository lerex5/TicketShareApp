package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class sellActivity extends AppCompatActivity {

    //private event newEvent;
    private Button Add,Add1;
    private ListView lv;
    private ArrayList<String> Tickets;
    private ArrayAdapter<String> TickAdapter;
    private String TAG = sellActivity.class.getSimpleName();
    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    //RealTime Database Connection
    private DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("Tickets");
    private DatabaseReference availabledb = FirebaseDatabase.getInstance().getReference("Available");
    private DatabaseReference userdb,moviedb;



/*

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_sell);

        Add=findViewById(R.id.btnAdd);
        lv=findViewById(R.id.lvTickets);
        Tickets=new ArrayList<>();
        TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);


      //  new GetmovieResults().execute();

       Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(sellActivity.this,addTickets.class);
                startActivity(intent);

            }
        });
    }

}
