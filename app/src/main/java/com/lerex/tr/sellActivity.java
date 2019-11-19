package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

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
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sell);

        ref= database.getReference(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        Add=findViewById(R.id.btnAdd);
        lv=findViewById(R.id.lvTickets);
        Tickets=new ArrayList<>();
        TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);
        lv.setAdapter(TickAdapter);

       Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(sellActivity.this,addTickets.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener movielistListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object object = dataSnapshot.getValue(Object.class);
                String json = new Gson().toJson(object);
                try {
                    TickAdapter.clear();
                    JSONObject usrTicketList = new JSONObject(json);
                    Iterator x = usrTicketList.keys();
                    while (x.hasNext()){
                        String key = (String) x.next();
                        TickAdapter.add(key);
                        System.out.println(key);
                    }
                    TickAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(sellActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        ref.addValueEventListener(movielistListener);

    }

}
