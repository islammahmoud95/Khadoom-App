package com.redray.khadoomhome.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.tickets.Activities.Ticket_details;
import com.redray.khadoomhome.tickets.models.Get_Tickets_Items;

import java.util.ArrayList;


public class Get_My_Tickets_adapter extends RecyclerView.Adapter<Get_My_Tickets_adapter.RecyclerViewHolder> {

    private Activity context;
    private ArrayList<Get_Tickets_Items> productsArrayList;



    public Get_My_Tickets_adapter(Activity context, ArrayList<Get_Tickets_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tickets_main, parent, false);


        return new RecyclerViewHolder(view, context, productsArrayList);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final String ticket_id = productsArrayList.get(position).getTicket_Id();

        Log.d("iddd", ticket_id);


        String name_str = productsArrayList.get(position).getTicket_Name();
        holder.subject_name.setText(name_str);


        String desc_ticket = productsArrayList.get(position).getTicket_desc();
        holder.subject_desc.setText(desc_ticket);

        String date_str = productsArrayList.get(position).getDate();
        holder.subject_date.setText(date_str);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_details = new Intent(context, Ticket_details.class);
                go_to_details.putExtra("Ticket_ID",ticket_id);
                context.startActivity(go_to_details);
            }
        });


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_name;
        TextView subject_desc;
        TextView subject_date;


        Context context;
        ArrayList<Get_Tickets_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_Tickets_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;



            subject_name = view.findViewById(R.id.title_ticket);
            subject_desc = view.findViewById(R.id.desc_ticket);
            subject_date = view.findViewById(R.id.time_ticket);



        }

    }



}