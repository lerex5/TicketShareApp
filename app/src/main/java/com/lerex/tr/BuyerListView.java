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


    List<TicketDetails> list;

    public BuyerListView(List<TicketDetails> list){
        this.list=list;
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


        TicketDetails ticketDetails=list.get(position);

        holder.date.setText("Date : "+ticketDetails.getDate());
        holder.cost.setText("Cost : "+ticketDetails.getCost());
        String a=String.valueOf(ticketDetails.getNumberOfTickets());
        holder.num.setText("Number of Tickets : "+a);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,cost,num;
        public ViewHolder(View view){

            super(view);



            date=view.findViewById(R.id.tvDate);
            cost=view.findViewById(R.id.tvCost);
            num=view.findViewById(R.id.tvNum);


        }
    }
}
/*
public class BuyerListView extends RecyclerView.Adapter<BuyerListView.MyViewHolder> {

    private List<TicketDetails> tdlist;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView buyDate,buyCost,buyNum;
        MyViewHolder(View v) {
            super(v);
            buyDate=v.findViewById(R.id.tvDate);
            buyCost=v.findViewById(R.id.tvCost);
            buyNum=v.findViewById(R.id.tvNum);
        }
    }

    BuyerListView(List<TicketDetails> tdlist){
        this.tdlist=tdlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TicketDetails td = tdlist.get(position);
        holder.buyDate.setText("Date : "+td.getDate());
        holder.buyCost.setText("Cost : "+td.getCost());
        String n="Number of tickets : "+ td.getNumberOfTickets();
        holder.buyNum.setText(n);
    }

    @Override
    public int getItemCount() {
        return tdlist.size();
    }

}
*/