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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*public class SellerListView extends ArrayAdapter<TicketDetails> {

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


}*/
public class SellerListView extends RecyclerView.Adapter<SellerListView.MyViewHolder> {

    private List<TicketDetails> tdlist;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView selMov,selDate,selCost,selNum;
        MyViewHolder(View v) {
            super(v);
            selMov=v.findViewById(R.id.selMov);
            selDate=v.findViewById(R.id.selDate);
            selCost=v.findViewById(R.id.selCost);
            selNum=v.findViewById(R.id.selNum);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TicketDetails td = tdlist.get(position);
        holder.selMov.setText("Movie name : "+td.getName());
        holder.selDate.setText("Date : "+td.getDate());
        holder.selCost.setText("Cost : "+td.getCost());
        String n="Number of tickets : "+ td.getNumberOfTickets();
        holder.selNum.setText(n);
    }

    @Override
    public int getItemCount() {
        return tdlist.size();
    }

}
