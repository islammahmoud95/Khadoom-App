package com.redray.khadoomhome.all_users.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redray.khadoomhome.PROVIDER.Activities.Order_Details;
import com.redray.khadoomhome.PROVIDER.Activities.Rating_View_Prov;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.Technical.Activities.Order_Details_tech;
import com.redray.khadoomhome.USER.Activities.Bill_Details_user;
import com.redray.khadoomhome.USER.Activities.Order_View_Details;
import com.redray.khadoomhome.USER.Activities.Post_Rating_Order;
import com.redray.khadoomhome.all_users.Models.Get_notify_Items;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import java.util.ArrayList;


public class Get_My_Notifications extends RecyclerView.Adapter<Get_My_Notifications.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_notify_Items> productsArrayList;

    private SessionManager sessionManager;


    public Get_My_Notifications(Activity context, ArrayList<Get_notify_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_notify, parent, false);

        sessionManager = new SessionManager(context);


        return new RecyclerViewHolder(view, context, productsArrayList);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        String notify_id = productsArrayList.get(position).getNotify_id();
        Log.d("id",notify_id);


        String title_str = productsArrayList.get(position).getTitle();
        holder.subject_Title.setText(title_str);

        final String content_str = productsArrayList.get(position).getContent();
        holder.subject_content.setText(content_str);


        String time_txt = productsArrayList.get(position).getTime_notify();
        holder.subject_Time.setText(time_txt);



        final String notify_type = productsArrayList.get(position).getNotify_Type();
        Log.d("type",notify_type);


        switch (notify_type) {
            case "2":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.green_color));
                break;
            case "3":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.green_color));
                break;
            case "4":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.md_yellow_600));
                break;
            case "5":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
                break;
            case "6":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.md_deep_purple_400));
                break;
            case "7":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.darkest_gray));
                break;
            case "0":
                // tech have not general
                // general notifications
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.md_red_A700));
                break;
            case "8":
                holder.line_color_view.setBackgroundColor(ContextCompat.getColor(context, R.color.md_deep_purple_400));
                break;
            default:
                break;
        }




        final String redirect_id_txt = productsArrayList.get(position).getRedirect_ID();
        Log.d("red_id",redirect_id_txt);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sessionManager.getUser_Type().equals("1") && notify_type.equals("2")) {

                    Intent intent = new Intent(context, Post_Rating_Order.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);

                } else if (sessionManager.getUser_Type().equals("2") && notify_type.equals("3")) {

                    Intent intent = new Intent(context, Order_Details.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);

                } else if (sessionManager.getUser_Type().equals("1") && notify_type.equals("4")) {

                    Intent intent = new Intent(context, Bill_Details_user.class);
                    intent.putExtra("BILL_ID", redirect_id_txt);
                    context.startActivity(intent);

                } else if (sessionManager.getUser_Type().equals("2") && notify_type.equals("5")) {

                    Intent intent = new Intent(context, Order_Details.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);


                } else if (sessionManager.getUser_Type().equals("3") && notify_type.equals("6")) {

                    Intent intent = new Intent(context, Order_Details_tech.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);

                } else if (sessionManager.getUser_Type().equals("2") && notify_type.equals("7")) {

                    Intent intent = new Intent(context, Rating_View_Prov.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);


                } else if (notify_type.equals("0")) {

                    // tech have not general
                    // general notifications
                    Utility.dialog_error(context, content_str);

                } else if (sessionManager.getUser_Type().equals("1") && notify_type.equals("8")) {

                    Intent intent = new Intent(context, Order_View_Details.class);
                    intent.putExtra("ORDER_ID", redirect_id_txt);
                    context.startActivity(intent);
                }



            }
        });


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_Title;
        TextView subject_content;
        TextView subject_Time;


        Context context;
        ArrayList<Get_notify_Items> productsArrayList;

        View line_color_view;

        RecyclerViewHolder(View view, Context context, final ArrayList<Get_notify_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_Title = view.findViewById(R.id.title_txt);
            subject_content = view.findViewById(R.id.content_txt);
            subject_Time = view.findViewById(R.id.time_txt);
            line_color_view = view.findViewById(R.id.line_color_view);

            // subject_name =  view.findViewById(R.id.orders_txt);


        }

    }


}