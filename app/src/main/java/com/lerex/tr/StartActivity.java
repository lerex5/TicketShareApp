package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    private String TAG = StartActivity.class.getSimpleName();
    private ArrayList<String> movies=new ArrayList<>();
    private ArrayList<String> cities=new ArrayList<>();
    private TextView ErrorText;
    private TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application
        setContentView(R.layout.activity_start);

        tinydb = new TinyDB(this);
        tinydb.deleteImage("Movies");

        ErrorText=findViewById(R.id.ErrorHandler);

        if(tinydb.getListString("Cities").isEmpty()) {
            CitiesJSON CityList = new CitiesJSON(getResources(), R.raw.cities);
            String JsonCityList = CityList.getJsonString();
            try {
                //JSONObject jsonObj = new JSONObject(JsonCityList);
                JSONArray citiesJSON = new JSONArray(JsonCityList);
                for (int i = 0; i < citiesJSON.length(); i++) {
                    JSONObject c = citiesJSON.getJSONObject(i);
                    String title = c.getString("City");
                    cities.add(title);
                }
                tinydb.putListString("Cities", cities);
            } catch (JSONException e) {
                e.printStackTrace();
                ErrorText.setText(e.getMessage());
            }
        }

        new GetmovieResults().execute();

    }

    private class GetmovieResults extends AsyncTask<Void, Void, Void> {
        int pages = 1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            for(int j=1;j<=pages;j++) {

                String url = "https://api.themoviedb.org/3/movie/now_playing?region=IN&page=" + j + "&language=en-US&api_key=cdb6543f56d4ae849f71ed220c46a080";
                String jsonStr = sh.makeServiceCall(url);

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
                                ErrorText.setText("Json parsing error: " + e.getMessage());

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
                            ErrorText.setText("Check Your Connection,\nCouldn't retrieve data from servers");
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(ErrorText.getText().toString().isEmpty()) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null && !tinydb.getString("CurCity").isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), FragHome.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else if (currentUser != null && tinydb.getString("CurCity").isEmpty()) {
                    startActivity(new Intent(StartActivity.this, LocationActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                finish();
            }
        }

    }
}
