package com.redray.khadoomhome.Technical.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.Technical.Activities.Bill_Details_tech;
import com.redray.khadoomhome.USER.Models.Get_Bills_Items;

import java.util.ArrayList;


public class Get_My_Bills_tech extends RecyclerView.Adapter<Get_My_Bills_tech.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_Bills_Items> productsArrayList;


    public Get_My_Bills_tech(Activity context, ArrayList<Get_Bills_Items> productsArrayList) {

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


        String status_bill_Value = productsArrayList.get(position).getBill_Status_Value();
        holder.subject_status_txt.setText(status_bill);

        if (status_bill_Value.equals("0")) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent go_to_detail = new Intent(context, Bill_Details_tech.class);
                    go_to_detail.putExtra("ORDER_ID", order_id);
                    context.startActivity(go_to_detail);
                }
            });
        } else {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DroidDialog.Builder(context)
                            .icon(R.drawable.khadoom_logo)
                            .title(context.getString(R.string.app_name))
                            .content("This Bill is already Paid and Finished")
                            .cancelable(true, true)
                            .positiveButton(context.getString(R.string.ok_dialog),
                                    new DroidDialog.onPositiveListener() {
                                        @Override
                                        public void onPositive(Dialog droidDialog) {

                                            droidDialog.dismiss();
                                        }
                                    })

                            .typeface("neosans-regular.ttf")
                            .animation(AnimUtils.AnimLeftRight)
                            .color(ContextCompat.getColor(context, R.color.dark_blue), ContextCompat.
                                            getColor(context, android.R.color.transparent),
                                    ContextCompat.getColor(context, R.color.dark_blue))
                            .divider(true, ContextCompat.getColor(context, R.color.light_blue))
                            .show();

                }
            });

        }

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
            subject_bill_detail = view.findViewById(R.id.bill_detail);
            subject_status_txt = view.findViewById(R.id.bill_stat);


        }

    }


}