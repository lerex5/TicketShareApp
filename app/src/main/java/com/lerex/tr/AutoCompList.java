package com.lerex.tr;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import java.util.List;

class AutoCompList extends ArrayAdapter<String> {

    public AutoCompList(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount(){
        if(super.getCount()<5){
            return super.getCount();
        }
        return 5;
    }
}