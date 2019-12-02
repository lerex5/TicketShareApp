package com.lerex.tr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class SellerListView extends RecyclerView.Adapter<SellerListView.MyViewHolder> {

    private List<TicketDetails> tdlist;
    final DatabaseReference tick = FirebaseDatabase.getInstance().getReference("Tickets");
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView selMov,selDate,selCost,selNum;
        Button callbtn;
        MyViewHolder(View v) {
            super(v);
            selMov=v.findViewById(R.id.selMov);
            selDate=v.findViewById(R.id.selDate);
            selCost=v.findViewById(R.id.selCost);
            selNum=v.findViewById(R.id.selNum);
            callbtn=v.findViewById(R.id.Call);
        }
    }

    SellerListView(List<TicketDetails> tdlist){
        this.tdlist=tdlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_seller, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        TicketDetails td = tdlist.get(position);
        holder.selMov.setText("Movie name : "+td.getName());
        holder.selDate.setText("Date : "+td.getDate());
        holder.selCost.setText("Cost : "+td.getCost());
        String n="Number of tickets : "+ td.getNumberOfTickets();
        holder.selNum.setText(n);
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tick.child(tdlist.get(position).getFirebaseId()).child("transactionMode").setValue(3);
                tdlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tdlist.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tdlist.size();
    }

}
