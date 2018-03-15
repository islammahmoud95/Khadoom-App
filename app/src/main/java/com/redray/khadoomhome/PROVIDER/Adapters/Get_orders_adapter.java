package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.PROVIDER.Activities.Choose_Technical;
import com.redray.khadoomhome.PROVIDER.Activities.Order_Details;
import com.redray.khadoomhome.PROVIDER.Models.Get_orders_Items;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Get_orders_adapter extends RecyclerView.Adapter<Get_orders_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_orders_Items> productsArrayList;

    // to identify call respone for adapter
    private String FragmentName;

    private KProgressHUD dialog_bar;

    private SessionManager session;

    public Get_orders_adapter(Activity context, ArrayList<Get_orders_Items> productsArrayList , String FragmentName) {

        this.context = context;
        this.productsArrayList = productsArrayList;
        this.FragmentName = FragmentName;

    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_orders_provid, parent, false);

        dialog_bar = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        session = new SessionManager(context);

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

        // this button used to get more to get refused reason
        holder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FragmentName.equals("REFUSED")){

                   // show dialog reasons of refuse

                    if (Check_Con.getInstance(context).isOnline()) {

                        dialog_bar.show();
                        Get_Refused_Reason(order_id);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(holder.itemView, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    }
                                });

                        snackbar.show();

                    }


                }else {

                    // got to details
                    Intent view_detail = new Intent(context , Order_Details.class);
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
                case "REFUSED":
                    //show something
                    Log.d("frag", "frag3");
                    chng_teck_btn.setVisibility(View.GONE);
                    details_btn.setText(R.string.reason);
                    break;
                case "APPROVED":
                    //show something
                    Log.d("frag", "frag4");
                    break;
            }

        }

    }


    private void Get_Refused_Reason(final String Order_ID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/getReasonRefusedOrder",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("respooo22", response);

                        try {


                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {


                                JSONObject stat_reg = obj.getJSONObject("Data");
                                String refuse_str = stat_reg.getString("reason");

                                dialog_bar.dismiss();

                                showRefusedDialog(refuse_str);


                            } else {

                                StringBuilder s = new StringBuilder(100);
                                String street ;
                                JSONArray st = obj.getJSONArray("Errors");
                                for (int i = 0; i < st.length(); i++) {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest", s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(context, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }


                        dialog_bar.dismiss();

                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog_bar.dismiss();

                        Toast.makeText(context, R.string.server_down, Toast.LENGTH_LONG).show();

                        Log.e("error", error.getMessage() + "");
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("order_id", Order_ID);

                Log.d("ProductsParams", params.toString());

                return params;
            }


            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
                session.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", session.getCookie());

                return headers;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private Dialog dialog;
    private void showRefusedDialog(String Refuse_txt) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() !=null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_view_refuse_reason);
        }

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