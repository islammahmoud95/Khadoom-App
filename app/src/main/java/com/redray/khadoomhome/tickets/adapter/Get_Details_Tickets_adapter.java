package com.redray.khadoomhome.tickets.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
import com.redray.khadoomhome.tickets.models.Get_Details_Tickets_Items;
import com.redray.khadoomhome.utilities.ImageLoader.ImageGesture;

import java.util.ArrayList;


public class Get_Details_Tickets_adapter extends RecyclerView.Adapter<Get_Details_Tickets_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_Details_Tickets_Items> productsArrayList;



    public Get_Details_Tickets_adapter(Activity context, ArrayList<Get_Details_Tickets_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_details_tickets, parent, false);


        return new RecyclerViewHolder(view, context, productsArrayList);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final String ticket_id = productsArrayList.get(position).getReply_Id();

        Log.d("iddd", ticket_id);

        final String sender_type = productsArrayList.get(position).getReplySender();
        Log.d("iddd", sender_type);

        //  sender 0 that meaning admin sent this replay ; 1 : user
        if (sender_type.equals("0")) {

            holder.card_view_replies.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
            holder.subject_content.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.subject_date.setTextColor(ContextCompat.getColor(context, R.color.white));

        }


        String content_str = productsArrayList.get(position).getReply_content();
        holder.subject_content.setText(content_str);


        String date_str = productsArrayList.get(position).getReplyDate();
        holder.subject_date.setText(date_str);

        final String img_reply = productsArrayList.get(position).getReplyImage();
        if (!img_reply.equals("")) {
            Glide.with(context).load(img_reply).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
            Log.d("dfdfdfsdfsdf", img_reply);

        } else {
            holder.subject_img.setVisibility(View.GONE);
        }

        holder.subject_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ImageGesture.class);
                i.putExtra("URL_OF_IMAGE", img_reply);
                Log.d("URL_OF_IMAGE",img_reply);
                context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_content;
        ImageView subject_img;
        TextView subject_date;
        CardView card_view_replies;

        Context context;
        ArrayList<Get_Details_Tickets_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_Details_Tickets_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_content = view.findViewById(R.id.reply_content_txt);
            subject_img = view.findViewById(R.id.reply_img);
            subject_date = view.findViewById(R.id.time_ticket);
            card_view_replies = view.findViewById(R.id.card_view_replies);


        }

    }


}