package com.lerex.tr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyerListView extends RecyclerView.Adapter<BuyerListView.ViewHolder> {

    private static final String TAG = "BuyerListView";

    List<TicketDetails> list;

    public BuyerListView(List<TicketDetails> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false);
        ViewHolder holder = new ViewHolder(view1);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        TicketDetails ticketDetails = list.get(position);
        final String phoneNum = ticketDetails.getSellerId();

        holder.date.setText(Html.fromHtml("<b>"+"Date : "+"</b>") + ticketDetails.getDate());
        holder.cost.setText(Html.fromHtml("<b>"+"Cost : "+"</b>") + ticketDetails.getCost());
        String a = String.valueOf(ticketDetails.getNumberOfTickets());
        holder.num.setText(Html.fromHtml("<b>"+"Number Of Tickets : "+"</b>") + a);
        holder.theatre.setText(Html.fromHtml("<b>"+"Theatre : "+"</b>")+ ticketDetails.getTheatre());
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Making Calls To Add Listener to Notice End of Call
                Context context = v.getContext();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ phoneNum));
                if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                context.startActivity(callIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,cost,num,theatre;
        Button callbtn;
        public ViewHolder(View view){

            super(view);

            date=view.findViewById(R.id.tvDate);
            cost=view.findViewById(R.id.tvCost);
            num=view.findViewById(R.id.tvNum);
            callbtn=view.findViewById(R.id.Call);
            theatre=view.findViewById(R.id.tvTheatre);

        }
    }
}
