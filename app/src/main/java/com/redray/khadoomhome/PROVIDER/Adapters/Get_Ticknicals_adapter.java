package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.redray.khadoomhome.PROVIDER.Models.Get_ticknicals_Items;
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


public class Get_Ticknicals_adapter extends RecyclerView.Adapter<Get_Ticknicals_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_ticknicals_Items> productsArrayList;

    private SessionManager sessionManager;

    private KProgressHUD dialog_bar;

    public Get_Ticknicals_adapter(Activity context, ArrayList<Get_ticknicals_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_teck, parent, false);

        dialog_bar = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(context);

        return new RecyclerViewHolder(view, context, productsArrayList);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder,  int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        final int pos = holder.getAdapterPosition();

        final String tech_id = productsArrayList.get(position).getTeck_Id();
        Log.d("iddd", tech_id);

        String name_str = productsArrayList.get(position).getTeck_Name();
        holder.subject_name.setText(name_str);

        String section_str = productsArrayList.get(position).getTeck_section();
        holder.subject_section.setText(section_str);

        String number_str = productsArrayList.get(position).getTeck_Number();
        holder.subject_number.setText(number_str);


        String tech_img = productsArrayList.get(position).getTeck_Img();
        if (tech_img.equals("")) {

            Glide.with(context).load(R.drawable.splash_logo_shape).thumbnail(0.5f)
                    .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
        } else {

            Glide.with(context).load(tech_img).thumbnail(0.5f)
                    .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.subject_img);
        }


        String tech_status = productsArrayList.get(position).getTeck_Status();
        if (tech_status.equals("1")) {
            holder.stat_tech_tog.setSelected(true);
            holder.stat_tech_tog.setChecked(true);
            holder.stat_tech_tog.setTextColor(ContextCompat.getColor(context, R.color.green_color));

        } else {
            holder.stat_tech_tog.setSelected(false);
            holder.stat_tech_tog.setChecked(false);
            holder.stat_tech_tog.setTextColor(ContextCompat.getColor(context, R.color.red_light));
        }

        holder.stat_tech_tog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(context).isOnline()) {

                    dialog_bar.show();

                    if (holder.stat_tech_tog.isChecked()){

                        change_tech_stat(pos,tech_id,"1");

                    }else {

                        change_tech_stat(pos,tech_id,"0");
                    }



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

            }
        });


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_name;
        TextView subject_section;
        TextView subject_number;
        CircleImageView subject_img;
        ToggleButton stat_tech_tog;

        Context context;
        ArrayList<Get_ticknicals_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_ticknicals_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_name = view.findViewById(R.id.title_order);
            subject_section = view.findViewById(R.id.section_txt);
            subject_img = view.findViewById(R.id.tech_image);
            subject_number = view.findViewById(R.id.number_txt);
            stat_tech_tog = view.findViewById(R.id.stat_tech_tog);


        }

    }




    private void change_tech_stat(final int position, final String tech_id, final String status_tech) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/updateStatusTech",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                JSONObject stat_reg = obj.getJSONObject("Data");
                                String status_tech = stat_reg.getString("status");

                                if (status_tech.equals("1")){

                                    productsArrayList.get(position).setTeck_Status("1");
                                    notifyDataSetChanged();

                                }else {

                                    productsArrayList.get(position).setTeck_Status("0");
                                    notifyDataSetChanged();

                                }


                                dialog_bar.dismiss();


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

                                notifyDataSetChanged();

                                dialog_bar.dismiss();

                                Utility.dialog_error(context, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog_bar.dismiss();
                Toast.makeText(context, R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                params.put("newStatus", status_tech);
                params.put("tech_id", tech_id);

                Log.e("Params", params.toString());

                return params;
            }

            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
                sessionManager.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", sessionManager.getCookie());

                return headers;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(stringRequest);
    }


}