package com.redray.khadoomhome.USER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Activities.Bill_Details_user;
import com.redray.khadoomhome.USER.Models.Get_Bills_Items;

import java.util.ArrayList;


public class Get_My_Bills extends RecyclerView.Adapter<Get_My_Bills.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_Bills_Items> productsArrayList;




    public Get_My_Bills(Activity context, ArrayList<Get_Bills_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bills, parent, false);

        return new RecyclerViewHolder(view, context, productsArrayList);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        String bill_id = productsArrayList.get(position).getBill_Id();
        holder.subject_bill_id.setText(bill_id);


        final String order_id = productsArrayList.get(position).getOrder_id();
        holder.subject_order_id.setText(order_id);


        String date_bill = productsArrayList.get(position).getDate();
        holder.subject_bill_date.setText(date_bill);


        String bill_amount = productsArrayList.get(position).getOrder_Amount();
        holder.subject_bill_amount.setText(bill_amount);


        String bill_detail = productsArrayList.get(position).getBill_Detail();
        holder.subject_bill_detail.setText(bill_detail);



        String status_bill = productsArrayList.get(position).getBill_Status();
        holder.subject_status_txt.setText(status_bill);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_detail = new Intent(context, Bill_Details_user.class);
                go_to_detail.putExtra("BILL_ID",order_id);
                context.startActivity(go_to_detail);
            }
        });


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_bill_id;
        TextView subject_order_id;
        TextView subject_bill_date;
        TextView subject_bill_amount;
        TextView subject_bill_detail;
        TextView subject_status_txt;


        Context context;
        ArrayList<Get_Bills_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_Bills_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_bill_id = view.findViewById(R.id.bill_id);
            subject_order_id = view.findViewById(R.id.order_id);
            subject_bill_date = view.findViewById(R.id.bill_date);
            subject_bill_amount = view.findViewById(R.id.bill_amount);
            subject_bill_detail= view.findViewById(R.id.bill_detail);
            subject_status_txt = view.findViewById(R.id.bill_stat);


            // subject_name =  view.findViewById(R.id.orders_txt);


        }

    }


}