package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redray.khadoomhome.PROVIDER.Models.Get_Parts_view_Items;
import com.redray.khadoomhome.R;

import java.util.ArrayList;


public class Get_Parts_view_adapter extends RecyclerView.Adapter<Get_Parts_view_adapter.RecyclerViewHolder> {

    private Activity context;

    public ArrayList<Get_Parts_view_Items> productsArrayList;




    public Get_Parts_view_adapter(Activity context, ArrayList<Get_Parts_view_Items> productsArrayList ) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }




    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_parts_view, parent, false);
        return new RecyclerViewHolder(view, context,productsArrayList);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder,  int position) {
      //  final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final String product_id = productsArrayList.get(position).getProductId();
        Log.d("sfsdf", product_id);


        final String name = productsArrayList.get(position).getProductName();
        holder.user_name.setText(name);


        holder.quantity.setText(productsArrayList.get(position).getQuantity());


    }



    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder  {


        TextView  user_name;
        TextView quantity;



        Context context;
        ArrayList<Get_Parts_view_Items> productsArrayList;


        public RecyclerViewHolder(View view, Context context, final ArrayList<Get_Parts_view_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            user_name =  view.findViewById(R.id.post_name);

            quantity =  view.findViewById(R.id.quantity_txt);

        }

    }

}