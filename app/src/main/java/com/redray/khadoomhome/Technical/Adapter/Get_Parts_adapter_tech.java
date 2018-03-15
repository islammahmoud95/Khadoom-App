package com.redray.khadoomhome.Technical.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.Technical.DataTransferInterface_total_num;
import com.redray.khadoomhome.Technical.Model.Get_Parts_Items_tech;

import java.util.ArrayList;


public class Get_Parts_adapter_tech extends RecyclerView.Adapter<Get_Parts_adapter_tech.RecyclerViewHolder> {

    private Activity context;

    public ArrayList<Get_Parts_Items_tech> productsArrayList;

    private DataTransferInterface_total_num dtInterface;


    public Get_Parts_adapter_tech(Activity context, ArrayList<Get_Parts_Items_tech> productsArrayList, DataTransferInterface_total_num dtInterface) {
        this.dtInterface = dtInterface;
        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_parts_tech, parent, false);
        return new RecyclerViewHolder(view, context, productsArrayList);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
//        final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;


        final int pos = position;

        final String product_id = productsArrayList.get(position).getProductId();
        Log.d("sfsdf", product_id);


        final String name = productsArrayList.get(position).getProductName();
        holder.user_name.setText(name);


        final String unit_cost = productsArrayList.get(position).getUnit_Cost();
        holder.unit_Cost.setText(unit_cost);


        final String total_unit_cost = productsArrayList.get(position).getTotal_unit_Cost();
        holder.total_Unit_Cost.setText(total_unit_cost);


        holder.quantity.setText(productsArrayList.get(position).getQuantity());



        holder.plus_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int quantityNum = Integer.parseInt(productsArrayList.get(pos).getQuantity());
                if (quantityNum < 99) {
                    quantityNum++;
                }

                holder.quantity.setText(String.valueOf(quantityNum));
                productsArrayList.get(pos).setQuantity(String.valueOf(quantityNum));


                // this for total unit cost
                //   int quantity = Integer.parseInt(holder.quantity.getText().toString());
                int unit_cost_num = Integer.parseInt(productsArrayList.get(pos).getUnit_Cost());
                int unit_total_cost = (quantityNum * unit_cost_num);
                holder.total_Unit_Cost.setText(String.valueOf(unit_total_cost));

                dtInterface.addValues_total_num(getSum());

            }


        });

        holder.min_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityNum = Integer.parseInt(productsArrayList.get(pos).getQuantity());
                quantityNum--;
                if (quantityNum == 0) {
                    quantityNum = 1;
                }


                holder.quantity.setText(String.valueOf(quantityNum));
                productsArrayList.get(pos).setQuantity(String.valueOf(quantityNum));


                // this for total unit cost
                //  int quantity = Integer.parseInt(productsArrayList.get(pos).getQuantity());
                int unit_cost_num = Integer.parseInt(productsArrayList.get(pos).getUnit_Cost());
                int unit_total_cost = (quantityNum * unit_cost_num);
                holder.total_Unit_Cost.setText(String.valueOf(unit_total_cost));

                dtInterface.addValues_total_num(getSum());


            }
        });


        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(productsArrayList.get(position).getcheckbox());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productsArrayList.get(holder.getAdapterPosition()).setcheckbox(isChecked);
                dtInterface.addValues_total_num(getSum());

                if (productsArrayList.get(holder.getAdapterPosition()).getcheckbox()) {

                    holder.checkBox.setChecked(true);
                    productsArrayList.get(holder.getAdapterPosition()).setcheckbox(true);

                } else {
                    holder.checkBox.setChecked(false);
                    productsArrayList.get(holder.getAdapterPosition()).setcheckbox(false);

                }
            }
        });


        //this method to send total parts for first view in adapter loop (bad preformace but required)
        if (productsArrayList.get(position).getcheckbox()){

            dtInterface.addValues_total_num(getSum());
        }

        holder.unit_Cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (holder.unit_Cost.getText().toString().trim().equals("")){
                    productsArrayList.get(holder.getAdapterPosition()).setUnit_Cost("0");
                    holder.unit_Cost.setText("0");
                    dtInterface.addValues_total_num(getSum());


                    int quantityNum = Integer.parseInt(productsArrayList.get(pos).getQuantity());
                    int unit_cost_num = Integer.parseInt(productsArrayList.get(pos).getUnit_Cost());
                    int unit_total_cost = (quantityNum * unit_cost_num);
                    holder.total_Unit_Cost.setText(String.valueOf(unit_total_cost));


                }else {

                    productsArrayList.get(holder.getAdapterPosition()).setUnit_Cost(String.valueOf(charSequence));
                    dtInterface.addValues_total_num(getSum());


                    int quantityNum = Integer.parseInt(productsArrayList.get(pos).getQuantity());
                    int unit_cost_num = Integer.parseInt(productsArrayList.get(pos).getUnit_Cost());
                    int unit_total_cost = (quantityNum * unit_cost_num);
                    holder.total_Unit_Cost.setText(String.valueOf(unit_total_cost));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        // THIS BLOCK USED TO GET FULL TOTAL SUM FIRST TIME
//        if (productsArrayList.get(holder.getAdapterPosition()).getcheckbox()) {
//            // dtInterface.setValues_total_num("5555");
//            // aray.add(productsArrayList.get(pos).getProductId());
//
//            // this for total unit cost
//            int quantity = Integer.parseInt(productsArrayList.get(pos).getQuantity());
//            int unit_cost_num = Integer.parseInt(productsArrayList.get(pos).getUnit_Cost());
//            int unit_total_cost = (quantity * unit_cost_num);
//            holder.total_Unit_Cost.setText(String.valueOf(unit_total_cost));
//
//            sum += quantity * unit_cost_num;
//            dtInterface.addValues_total_num(+sum);
//        }

    }

 //   int sum ;

    private int getSum() {
        int sum = 0;
        for (Get_Parts_Items_tech obj : productsArrayList) {
            if (obj.getcheckbox()) {

                float quant = Integer.parseInt(obj.getQuantity());
                float cost = Integer.parseInt(obj.getUnit_Cost());

                sum += quant * cost;
            }
        }

        return sum;
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

//    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        CheckBox checkBox;
        TextView quantity;

        EditText unit_Cost;
        TextView total_Unit_Cost;


        ImageButton plus_qty_btn, min_qty_btn;

        Context context;
        ArrayList<Get_Parts_Items_tech> productsArrayList;


        public RecyclerViewHolder(View view, Context context, final ArrayList<Get_Parts_Items_tech> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;

//            view.setOnClickListener(this);


            user_name =  view.findViewById(R.id.post_name);

            checkBox =  view.findViewById(R.id.checkboxx);

            quantity =  view.findViewById(R.id.quantity_txt);

            unit_Cost =  view.findViewById(R.id.unit_cost_txt);

            total_Unit_Cost =  view.findViewById(R.id.total_unit_cost_txt);

            plus_qty_btn =  view.findViewById(R.id.plus_qty_btn);

            min_qty_btn =  view.findViewById(R.id.min_qty_btn);





        }


//        @Override
//        public void onClick(View v) {
//
//            int position = getAdapterPosition();
//
//            Intent intent =  new Intent(context, ProductDetailsActivity.class);
//            intent.putExtra("PRODUCT_ID",productsArrayList.get(position).getProductId());
//            intent.putExtra("PRODUCT_NAME",productsArrayList.get(position).getProductName());
//            intent.putExtra("PRODUCT_PRICE",productsArrayList.get(position).getProductPrice());
//            intent.putExtra("PRODUCT_TYPE",productsArrayList.get(position).getProductType());
//            intent.putExtra("PRODUCT_IMAGE",productsArrayList.get(position).getProductImage());
//            intent.putExtra("PRODUCT_SHARE",productsArrayList.get(position).getProductShare());
//            intent.putExtra("PRODUCT_MEASURE_UNIT",productsArrayList.get(position).getProductMeasurementUnit());
//            intent.putExtra("PRODUCT_CURRENCY",productsArrayList.get(position).getProductCurrency());
//            intent.putExtra("PRODUCT_DETAILS",productsArrayList.get(position).getProductDetails());
//            intent.putExtra("SHOP_NAME",productsArrayList.get(position).getProductShopName());
//            intent.putExtra("SHOP_ICON",productsArrayList.get(position).getProductIcon());
//            context.startActivity(intent);
//
//        }


    }

}