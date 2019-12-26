package com.lerex.tr;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class SellerListView extends RecyclerView.Adapter<SellerListView.MyViewHolder> {

    private List<TicketDetails> tdlist;
    private Context mctxt;
    final DatabaseReference tick = FirebaseDatabase.getInstance().getReference("Tickets");
    final DatabaseReference soldTick=FirebaseDatabase.getInstance().getReference("Sold");
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView selMov,selDate,selCost,selNum,eventTime;
        Button callbtn,soldBtn;
        MyViewHolder(View v) {
            super(v);
            selMov=v.findViewById(R.id.selMov);
            selDate=v.findViewById(R.id.selDate);
            selCost=v.findViewById(R.id.selCost);
            selNum=v.findViewById(R.id.selNum);
            eventTime=v.findViewById(R.id.selTime);
            callbtn=v.findViewById(R.id.Call);
            soldBtn=v.findViewById(R.id.Sold);
        }
    }

    SellerListView(List<TicketDetails> tdlist,Context mctxt){
        this.tdlist=tdlist;
        this.mctxt=mctxt;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_seller, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final TicketDetails td = tdlist.get(position);
        holder.selMov.setText(td.getName());
        holder.selDate.setText(" "+td.getDate());
        holder.selCost.setText(" "+td.getCost());
        holder.eventTime.setText(td.getTime());
        final String n=" "+ td.getNumberOfTickets();
        holder.selNum.setText(n);
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(mctxt).inflate(R.layout.delete_popup,null);
                final PopupWindow popupWindow=new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                Button ok=view.findViewById(R.id.btnOk);
                Button cancel=view.findViewById(R.id.btnCancel);
                Button close=view.findViewById(R.id.Close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tick.child(tdlist.get(position).getFirebaseId()).child("transactionMode").setValue(3);
                        tdlist.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tdlist.size());
                        popupWindow.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(view,Gravity.CENTER,0,0);

            }
        });
        holder.soldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(mctxt).inflate(R.layout.sold_popup,null);
                final PopupWindow popupWindow = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                Button ok=view.findViewById(R.id.btnOk);
                Button cancel=view.findViewById(R.id.btnCancel);
                Button close=view.findViewById(R.id.Close);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         String id=tick.child(tdlist.get(position).getFirebaseId()).getKey();
                         soldTick.child(id).setValue("");
                         tick.child(tdlist.get(position).getFirebaseId()).child("transactionMode").setValue(2);
                         tdlist.remove(position);
                         notifyItemRemoved(position);
                         notifyItemRangeChanged(position, tdlist.size());
                         popupWindow.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(view,Gravity.CENTER,0,0);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tdlist.size();
    }

}
//33aab8c2