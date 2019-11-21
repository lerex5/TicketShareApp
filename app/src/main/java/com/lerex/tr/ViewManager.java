package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import com.lerex.tr.TinyDB;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewManager extends AppCompatActivity {

    private String TAG = ViewManager.class.getSimpleName();
    private ArrayList<String> movies=new ArrayList<>();
    private TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_view_manager);



        tinydb = new TinyDB(this);
        tinydb.deleteImage("Movies");

        Button sell=findViewById(R.id.btnSell);
        Button buy=findViewById(R.id.btnBuy);


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewManager.this,sellActivity.class));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewManager.this,buyerActivity.class));
            }
        });

    }
    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        finish();//I guess this does it.
    }


    private class GetmovieResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(ViewManager.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

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
                        movies.add(moviesReleased.get("title"));

                    }
                    tinydb.putListString("Movies",movies);
                    System.out.println(tinydb.getListString("Movies"));
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
