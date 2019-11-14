package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private DatabaseReference availabledb = FirebaseDatabase.getInstance().getReference("Available");

    private class event {
        public String eventName,cost,sellerId,buyerId,eventDate;
        public int noOfTickets;

        public  event(String eventName,String cost,String eventDate,int noOfTickets,String sellerId){
            this.eventDate=eventDate;
            this.eventName=eventName;
            this.cost=cost;
            this.noOfTickets=noOfTickets;
            this.sellerId=sellerId;
        }
    }

    protected void addTicket(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        AutoCompleteTextView eName = findViewById(R.id.etRef1);
        EditText eDate = findViewById(R.id.etDate);
        EditText eCost = findViewById(R.id.etCost);
        EditText eNo = findViewById(R.id.etNumber);
        event newEvent = new event(eName.getText().toString(),eCost.getText().toString(),eDate.getText().toString(),Integer.valueOf(eNo.getText().toString()),curuser);

        DatabaseReference moviedb = FirebaseDatabase.getInstance().getReference(eName.getText().toString());
        DatabaseReference userdb = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
        moviedb.child(key).setValue("");
        userdb.child(key).setValue("");
        availabledb.child(key).setValue("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tickets);

        Tickets=new ArrayList<>();
        TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);
        new GetmovieResults().execute();


        Button add1 = findViewById(R.id.btnAdd1);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTicket();
                Intent intent=new Intent(addTickets.this,sellActivity.class);
                startActivity(intent);
            }
        });

        AutoCompleteTextView act =findViewById(R.id.etRef1);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(addTickets.this,android.R.layout.simple_dropdown_item_1line,Tickets);
        act.setAdapter(adapter);
    }
    private class GetmovieResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(addTickets.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.themoviedb.org/3/movie/now_playing?region=IN&page=1&language=en-US&api_key=cdb6543f56d4ae849f71ed220c46a080";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray movieResults = jsonObj.getJSONArray("results");

                    // looping through All movieResults
                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject c = movieResults.getJSONObject(i);
                        String title = c.getString("title");

                        // tmp hash map for single contact
                        HashMap<String, String> moviesReleased = new HashMap<>();

                        // adding each child node to HashMap key => value
                        moviesReleased.put("title", title);
                        Tickets.add(moviesReleased.get("title"));
                        TickAdapter.notifyDataSetChanged();


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //To Add Modifications For Drop Down List Box
        }

    }

}
