package com.redray.khadoomhome.USER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Models.Get_Parts_Items;

import java.util.ArrayList;



public class Get_Parts_adapter extends RecyclerView.Adapter<Get_Parts_adapter.RecyclerViewHolder> {

    private Activity context;

    public ArrayList<Get_Parts_Items> productsArrayList;




    public Get_Parts_adapter(Activity context, ArrayList<Get_Parts_Items> productsArrayList ) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_parts, parent, false);
        return new RecyclerViewHolder(view, context,productsArrayList);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder,  int position) {
      //  final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final int pos = holder.getAdapterPosition();

        final String product_id = productsArrayList.get(position).getProductId();
        Log.d("sfsdf", product_id);


        final String name = productsArrayList.get(position).getProductName();
        holder.user_name.setText(name);


        holder.quantity.setText(productsArrayList.get(position).getQuantity());

        holder.plus_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantityNum =Integer.parseInt(productsArrayList.get(pos).getQuantity());
                if (quantityNum < 99) {
                    quantityNum ++;
                }

                holder.quantity.setText(String.valueOf(quantityNum));
                productsArrayList.get(pos).setQuantity(String.valueOf(quantityNum));
            }
        });

        holder.min_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantityNum =Integer.parseInt(productsArrayList.get(pos).getQuantity());
                quantityNum --;
                if (quantityNum == 0) {
                    quantityNum = 1;
                }

                holder.quantity.setText(String.valueOf(quantityNum));
                productsArrayList.get(pos).setQuantity(String.valueOf(quantityNum));
            }
        });


        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(productsArrayList.get(position).getcheckbox());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productsArrayList.get(holder.getAdapterPosition()).setcheckbox(isChecked);


                if (productsArrayList.get(holder.getAdapterPosition()).getcheckbox()) {

                    holder.checkBox.setChecked(true);

                } else {
                    holder.checkBox.setChecked(false);


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
        TextView quantity;

        ImageButton plus_qty_btn , min_qty_btn;

        Context context;
        ArrayList<Get_Parts_Items> productsArrayList;


        public RecyclerViewHolder(View view, Context context, final ArrayList<Get_Parts_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            user_name =  view.findViewById(R.id.post_name);

            checkBox =  view.findViewById(R.id.checkboxx);

            quantity =  view.findViewById(R.id.quantity_txt);

            plus_qty_btn =  view.findViewById(R.id.plus_qty_btn);

            min_qty_btn =  view.findViewById(R.id.min_qty_btn);

        }

    }

}