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

public class SellerListView extends ArrayAdapter<TicketDetails> {

    private Context mctx;
    private int resource;
    private List<TicketDetails> tdlist;

    protected SellerListView(Context mctx,int resource,List<TicketDetails> tdlist){
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

        View view=inflater.inflate(R.layout.list_seller,null);

        TextView selMov=view.findViewById(R.id.selMov);
        TextView selDate=view.findViewById(R.id.selDate);
        TextView selCost=view.findViewById(R.id.selCost);
        TextView selNum=view.findViewById(R.id.selNum);

        TicketDetails td=tdlist.get(position);

        selMov.setText("Movie name : "+td.getName());
        selDate.setText("Date : "+td.getDate());
        selCost.setText("Cost : "+td.getCost());
        String n=String.valueOf(td.getNumberOfTickets());
        selNum.setText("Number of tickets : "+n);

        return view;

    }


}
