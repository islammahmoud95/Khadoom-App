package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redray.khadoomhome.PROVIDER.Models.Get_all_services_Items;
import com.redray.khadoomhome.R;

import java.util.ArrayList;



public class Get_all_services_adapter extends RecyclerView.Adapter<Get_all_services_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_all_services_Items> productsArrayList;


    public Get_all_services_adapter(Activity context, ArrayList<Get_all_services_Items> productsArrayList ) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_all_services_prov, parent, false);



        return new RecyclerViewHolder(view, context, productsArrayList);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        String main_serv_str = productsArrayList.get(position).getMain_SERV();
        holder.subject_main_serv.setText(main_serv_str);

        String sub_ser_str = productsArrayList.get(position).getSub_SERV();
        holder.subject_sub_serv.setText(sub_ser_str);

        String country_str = productsArrayList.get(position).getCountry_SERV();
        holder.subject_country.setText(country_str);

        String cities_str = productsArrayList.get(position).getCities_SERV();
        holder.subject_cities.setText(cities_str);


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

     class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_main_serv;
        TextView subject_sub_serv;
        TextView subject_country;
         TextView subject_cities;


        Context context;
        ArrayList<Get_all_services_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_all_services_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_main_serv = view.findViewById(R.id.main_serv);
            subject_sub_serv = view.findViewById(R.id.sub_serv);
            subject_country = view.findViewById(R.id.county_serv);
            subject_cities = view.findViewById(R.id.city_serv);

        }

    }



}