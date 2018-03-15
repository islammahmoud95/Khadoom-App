package com.redray.khadoomhome.Technical.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.redray.khadoomhome.PROVIDER.Activities.Choose_Technical;
import com.redray.khadoomhome.PROVIDER.Models.Get_orders_Items;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.Technical.Activities.Order_Details_tech;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Get_orders_tech_adapter extends RecyclerView.Adapter<Get_orders_tech_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_orders_Items> productsArrayList;

    // to identify call respone for adapter
    private String FragmentName;

    public Get_orders_tech_adapter(Activity context, ArrayList<Get_orders_Items> productsArrayList , String FragmentName) {

        this.context = context;
        this.productsArrayList = productsArrayList;
        this.FragmentName = FragmentName;

    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_orders_provid, parent, false);

        return new RecyclerViewHolder(view, context, productsArrayList);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        final String order_id = productsArrayList.get(position).getOrder_Id();
        Log.d("iddd", order_id);
        holder.order_id_txt.setText(order_id);


        String name_str = productsArrayList.get(position).getOrder_Name();
        holder.subject_name.setText(name_str);

        String serv_ticket = productsArrayList.get(position).getOrder_serv();
        holder.subject_service.setText(serv_ticket);

        String location_str = productsArrayList.get(position).getOrder_Location();
        holder.subject_location.setText(location_str);

        String date_str = productsArrayList.get(position).getDate();
        holder.subject_date.setText(date_str);



        String order_img = productsArrayList.get(position).getOrder_Img();
        if (order_img.equals("")) {

            Glide.with(context).load(R.drawable.splash_logo_shape).thumbnail(0.5f)
                    .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
        } else {

            Glide.with(context).load(order_img).thumbnail(0.5f)
                    .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
        }



        holder.chng_teck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // here workaround we put key to make different between
                // change technical
                // and accept order and assign it to technical
                //Assign_Type  ASSIGN for accepted
                //Assign_Type change

                Intent chng_tech = new Intent(context , Choose_Technical.class);
                chng_tech.putExtra("ORDER_ID",order_id);
                chng_tech.putExtra("Assign_Type","CHANGE");
                context.startActivity(chng_tech);



            }
        });

        holder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FragmentName.equals("FINISHED")){

                   // show dialog finished


                    show_finished_Dialog("This Order has been Finished And No More Details for it");




                }else {

                    // got to details
                    Intent view_detail = new Intent(context , Order_Details_tech.class);
                    view_detail.putExtra("ORDER_ID",order_id);
                    context.startActivity(view_detail);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

     class RecyclerViewHolder extends RecyclerView.ViewHolder {


         TextView order_id_txt;
        TextView subject_name;
        TextView subject_service;
        TextView subject_location;
        TextView subject_date;


        CircleImageView subject_img;

        Button chng_teck_btn , details_btn;
        Context context;
        ArrayList<Get_orders_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_orders_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;

            order_id_txt = view.findViewById(R.id.order_id_txt);
            subject_name = view.findViewById(R.id.title_order);
            subject_service = view.findViewById(R.id.service_txt);
            subject_location = view.findViewById(R.id.location_txt);
            subject_img = view.findViewById(R.id.order_image);
            subject_date = view.findViewById(R.id.time_txt);

            chng_teck_btn = view.findViewById(R.id.chng_teck_btn);
            details_btn = view.findViewById(R.id.details_btn);


            switch (FragmentName) {
                case "NEW":
                    //hide something
                    Log.d("frag", "frag1");
                    chng_teck_btn.setVisibility(View.GONE);
                    break;
                case "FINISHED":
                    //show something
                    Log.d("frag", "frag2");
                    chng_teck_btn.setVisibility(View.GONE);
                    break;
            }

        }

    }






    private Dialog dialog;
    private void show_finished_Dialog(String Refuse_txt) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() !=null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_view_refuse_reason);
        }

        TextView head_dialog = dialog.findViewById(R.id.head_dialog);
        head_dialog.setText(R.string.app_name);

        TextView refuse_reason_view =  dialog.findViewById(R.id.reason_ref_txt);
        refuse_reason_view.setText(Refuse_txt);

        Button close_btn =  dialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        dialog.show();

    }

}