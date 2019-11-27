package com.lerex.tr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;

        switch (position){
            case 0:
                fragment=new sellActivity();
                break;

            case 1:
                fragment=new buyerActivity();
                break;

            case 2:
                fragment=new accountActivity();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
