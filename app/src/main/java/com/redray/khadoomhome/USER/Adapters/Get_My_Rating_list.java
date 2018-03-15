package com.redray.khadoomhome.USER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Models.Get_rating_Items;

import java.util.ArrayList;


public class Get_My_Rating_list extends RecyclerView.Adapter<Get_My_Rating_list.RecyclerViewHolder> {

    private Activity context;
    public ArrayList<Get_rating_Items> productsArrayList;


    private String FragmentName;


    public Get_My_Rating_list(Activity context, ArrayList<Get_rating_Items> productsArrayList, String FragmentName) {

        this.context = context;
        this.productsArrayList = productsArrayList;
        this.FragmentName = FragmentName;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_rates, parent, false);


        return new RecyclerViewHolder(view, context, productsArrayList);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        final int pos = position;


        String rate_id = productsArrayList.get(position).getRate_ID();
        Log.d("rat_id", rate_id);


        final String rate_title = productsArrayList.get(position).getRate_name();
        holder.subject_rate_title.setText(rate_title);


        double rate_num = productsArrayList.get(position).getRate_Num();
        holder.subject_rating.setRating((float) rate_num);

        // HERE ADAPTER CAN CHANGE RATING BAR TO USER RATE
        if (FragmentName.equals("POST")) {

            holder.subject_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {

               //     int numStars = ratingBar.getNumStars();
                    productsArrayList.get(pos).setRate_Num(rating);

                }
            });

            // HERE PREVENT PROVIDER TO CHANGE RATING BAR
        } else if (FragmentName.equals("VIEW")) {

            holder.subject_rating.setIsIndicator(true);

        }


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_rate_title;
        RatingBar subject_rating;


        Context context;
        ArrayList<Get_rating_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_rating_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;

            subject_rate_title = view.findViewById(R.id.title_rate);
            subject_rating = view.findViewById(R.id.ratingBar);


        }

    }

}