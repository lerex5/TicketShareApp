package com.lerex.tr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyerListView extends RecyclerView.Adapter<BuyerListView.ViewHolder> {

    private static final String TAG="BuyerListView";

    private ArrayList<String> DateList;
    private ArrayList<String> CostList;
    private ArrayList<Integer> NumList;
    private Context mContext;

    public BuyerListView(ArrayList<String> dateList, ArrayList<String> costList, ArrayList<Integer> numList, Context mContext) {
        DateList = dateList;
        CostList = costList;
        NumList = numList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview,parent,false);
        ViewHolder holder=new ViewHolder(view1);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG,"onBindViewHolder:called");
        holder.tvDate.setText("Date : "+DateList.get(position));
        holder.tvCost.setText("Cost : "+CostList.get(position));
        String a=String.valueOf(NumList.get(position));
        holder.tvNum.setText("Number of Tickets : "+a);


    }

    @Override
    public int getItemCount() {
        return DateList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvCost,tvNum;
        Button btn;
        RelativeLayout parent;

        public ViewHolder(View view){
            super(view);

            tvDate=view.findViewById(R.id.tvDate);
            tvCost=view.findViewById(R.id.tvCost);
            tvNum=view.findViewById(R.id.tvNum);
            parent=view.findViewById(R.id.parent);

        }
    }
}