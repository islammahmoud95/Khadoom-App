package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.redray.khadoomhome.PROVIDER.DataTransferInterface_Add_Serv;
import com.redray.khadoomhome.PROVIDER.Models.Add_service_items;
import com.redray.khadoomhome.R;

import java.util.ArrayList;


public class Add_service_cities extends RecyclerView.Adapter<Add_service_cities.RecyclerViewHolder> {

    private Activity context;

    public ArrayList<Add_service_items> productsArrayList;

    private ArrayList<String> aray = new ArrayList<>();

    private DataTransferInterface_Add_Serv dtInterface;



    public Add_service_cities(Activity context, ArrayList<Add_service_items> productsArrayList , DataTransferInterface_Add_Serv dtInterface) {
        this.dtInterface = dtInterface;
        this.context = context;
        this.productsArrayList = productsArrayList;


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_cities_list, parent, false);
        return new RecyclerViewHolder(view, context,productsArrayList);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder,  int position) {
    //    final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final int pos = position;


        final String product_id= productsArrayList.get(position).getProductId();
        Log.d("sfsdf",product_id);


        final String name= productsArrayList.get(position).getProductName();
        holder.user_name.setText(name);




        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(productsArrayList.get(position).getcheckbox());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productsArrayList.get(holder.getAdapterPosition()).setcheckbox(isChecked);


                if (productsArrayList.get(holder.getAdapterPosition()).getcheckbox())
                {
                    aray.add(productsArrayList.get(pos).getProductId());
                    dtInterface.setValues_cities(aray);
                }else {
                        aray.remove(productsArrayList.get(pos).getProductId());
                        dtInterface.setValues_cities(aray);
                    }

            }
        });


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView  user_name;
        CheckBox checkBox;

        Context context;
        ArrayList<Add_service_items> productsArrayList;


        public RecyclerViewHolder(View view, Context context, final ArrayList<Add_service_items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            user_name =  view.findViewById(R.id.post_name);

            checkBox  =  view.findViewById(R.id.checkboxx);



        }


    }

}