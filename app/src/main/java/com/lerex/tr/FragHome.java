package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragHome extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

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


}

