package com.lerex.tr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BuyerListView extends ArrayAdapter<TicketDetails> {

    private Context mctx;
    private int resource;
    private List<TicketDetails> tdlist;

    protected BuyerListView(Context mctx,int resource,List<TicketDetails> tdlist){
        super(mctx,resource,tdlist);

        this.mctx=mctx;
        this.resource=resource;
        this.tdlist=tdlist;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(mctx);

        View view=inflater.inflate(R.layout.activity_listview,null);

        TextView tvDate=view.findViewById(R.id.tvDate);
        TextView tvCost=view.findViewById(R.id.tvCost);
        TextView tvNum=view.findViewById(R.id.tvNum);

        TicketDetails td=tdlist.get(position);

        tvDate.setText("Date : "+td.getDate());
        tvCost.setText("Cost : "+td.getCost());
        String n=String.valueOf(td.getNumberOfTickets());
        tvNum.setText("Number of tickets : "+n);

        return view;

    }


}
