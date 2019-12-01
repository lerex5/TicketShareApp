package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FragHome extends AppCompatActivity {

    private ViewPager viewPager;
    private String TAG = FragHome.class.getSimpleName();
    private ArrayList<String> movies=new ArrayList<>();
    private TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        tinydb = new TinyDB(this);
        tinydb.deleteImage("Movies");
        new GetmovieResults().execute();

        setContentView(R.layout.activity_frag_home);
        viewPager=findViewById(R.id.viewPager);
        PagerViewAdapter pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager(),PagerViewAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerViewAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);


    }


    private BottomNavigationView.OnNavigationItemSelectedListener nav=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int pos=0;

            switch (menuItem.getItemId()){
                case R.id.seller:
                    pos=0;
                    break;

                case R.id.buyer:
                    pos=1;
                    break;

                case R.id.acct:
                    pos=2;
                    break;

            }
            viewPager.setCurrentItem(pos);
            return true;
        }
    };

    private class GetmovieResults extends AsyncTask<Void, Void, Void> {

        int pages = 1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(FragHome.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            for(int j=1;j<=pages;j++) {
                String url = "https://api.themoviedb.org/3/movie/now_playing?region=IN&page=" + j + "&language=en-US&api_key=cdb6543f56d4ae849f71ed220c46a080";
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + j);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray movieResults = jsonObj.getJSONArray("results");
                        pages = jsonObj.getInt("total_pages");
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
                        tinydb.putListString("Movies", movies);
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
            }
                return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //To Add Modifications For Drop Down List Box
            Toast.makeText(FragHome.this, "Json Data downloaded", Toast.LENGTH_LONG).show();
        }

    }
}

