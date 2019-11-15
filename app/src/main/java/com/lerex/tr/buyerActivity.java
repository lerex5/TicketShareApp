package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class buyerActivity extends AppCompatActivity {



    private String TAG = buyerActivity.class.getSimpleName();
    private ArrayList<String> Tickets;
    private ArrayList<String> Avail;
    private ArrayAdapter<String> TickAdapter;
    private ArrayAdapter<String> SearchAdapter;
    private ArrayAdapter<String> AvailAdapter;
    private String mov;
    protected AutoCompleteTextView search;
    private ListView available;


    //Auth
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        Tickets=new ArrayList<>();
        TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);
        new buyerActivity.GetmovieResults().execute();


        search=findViewById(R.id.tvSearch);
        available=findViewById(R.id.lvAvailable);
        Button search1=findViewById(R.id.btnSearch);

        SearchAdapter=new ArrayAdapter<>(buyerActivity.this,android.R.layout.simple_dropdown_item_1line,Tickets);
        search.setAdapter(SearchAdapter);
        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avail.clear();
                GetAvailable();
                AvailAdapter.notifyDataSetChanged();
            }
        });


        Avail=new ArrayList<>();
        AvailAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,R.id.label,Avail);
        available.setAdapter(AvailAdapter);



    }
    protected void GetAvailable(){
        mov=search.getEditableText().toString();



        FirebaseDatabase.getInstance().goOnline();
        DatabaseReference avdb = FirebaseDatabase.getInstance().getReference(mov);
        avdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String a=postSnapshot.getValue(String.class);
                    Avail.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private class GetmovieResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(buyerActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
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
