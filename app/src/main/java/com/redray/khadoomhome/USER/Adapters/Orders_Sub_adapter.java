package com.redray.khadoomhome.USER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Activities.Make_Order_Activity;
import com.redray.khadoomhome.USER.Models.My_Orders_Sub_Items;

import java.util.ArrayList;


public class Orders_Sub_adapter extends RecyclerView.Adapter<Orders_Sub_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<My_Orders_Sub_Items> productsArrayList;


    public Orders_Sub_adapter(Activity context, ArrayList<My_Orders_Sub_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_sub_item, parent, false);
        return new RecyclerViewHolder(view, context, productsArrayList);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final String order_id = productsArrayList.get(position).getOrdersId();

        Log.d("sfsdf", order_id);

        final String order_main_id = productsArrayList.get(position).getOrders_main_Id();

        Log.d("sfsdf", order_main_id);


        String icon = productsArrayList.get(position).getimage_Order();
        if (!icon.equals("")) {
            Glide.with(context).load(icon).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
            Log.d("dfdfdfsdfsdf", icon);

        }


        holder.subject_name.setText(productsArrayList.get(position).getOrdersName());


        holder.subject_price.setText(productsArrayList.get(position).getorder_fees());

        (holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sub_order_intent = new Intent(context, Make_Order_Activity.class);

                sub_order_intent.putExtra("SUB_SERVICE_ID", order_id);

                context.startActivity(sub_order_intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_name;
        ImageView subject_img;
        TextView subject_price;


        Context context;
        ArrayList<My_Orders_Sub_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<My_Orders_Sub_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_img = view.findViewById(R.id.orders_img);

            subject_name = view.findViewById(R.id.orders_txt);

            subject_price = view.findViewById(R.id.orders_price);


        }

    }


}