package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

class event {
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

public class sellActivity extends AppCompatActivity {

    private event newEvent;
    private View customView;
    private PopupWindow aPop;
    private ConstraintLayout sellerLayout;
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

    protected void addTickets(){
        String key = mydb.push().getKey();
        String curuser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        EditText eName = customView.findViewById(R.id.etName);
        EditText eDate = customView.findViewById(R.id.etDate);
        EditText eCost = customView.findViewById(R.id.etCost);
        EditText eNo = customView.findViewById(R.id.etNumber);
        newEvent = new event(eName.getText().toString(),eCost.getText().toString(),eDate.getText().toString(),Integer.valueOf(eNo.getText().toString()),curuser);

        moviedb=FirebaseDatabase.getInstance().getReference(eName.getText().toString());
        userdb= FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        mydb.child(Objects.requireNonNull(key)).setValue(newEvent);
        moviedb.child(key).setValue("");
        userdb.child(key).setValue("");
        availabledb.child(key).setValue("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sell);

        sellerLayout=findViewById(R.id.sellerLa);

        Add=findViewById(R.id.btnAdd);


       /* Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) sellActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                customView = Objects.requireNonNull(layoutInflater).inflate(R.layout.addticket,null);

                //instantiate popup window
                aPop = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                aPop.setFocusable(true);
                aPop.update();
                aPop.showAtLocation(sellerLayout, Gravity.CENTER, 0, 0);

                Add1=customView.findViewById(R.id.btnAdd1);
                Add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTickets();
                        aPop.dismiss();//Dismiss pop up
                        Tickets.add(newEvent.eventName);
                        TickAdapter.notifyDataSetChanged();
                        lv.setAdapter(TickAdapter);

                    }
                });
            }
        });*/
    }


    private class GetmovieResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(sellActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

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

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),
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
            /*lv=findViewById(R.id.lvTickets);
            Tickets=new ArrayList<>();
            TickAdapter=new ArrayAdapter<>(this,R.layout.activity_listview,Tickets);*/

        }
    }
}
