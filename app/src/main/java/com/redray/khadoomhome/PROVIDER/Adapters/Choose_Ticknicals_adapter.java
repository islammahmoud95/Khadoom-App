package com.redray.khadoomhome.PROVIDER.Adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.redray.khadoomhome.PROVIDER.Models.Get_ticknicals_Items;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Choose_Ticknicals_adapter extends RecyclerView.Adapter<Choose_Ticknicals_adapter.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_ticknicals_Items> productsArrayList;

    KProgressHUD dialog_bar;

    String order_id;
    String assign_Type;

    SessionManager session;

    public Choose_Ticknicals_adapter(Activity context, ArrayList<Get_ticknicals_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

        Intent intent = context.getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("ORDER_ID");
            assign_Type = intent.getStringExtra("Assign_Type");
            Log.d("aaaaaaaaaaa", order_id);
        }

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_choose_teck, parent, false);

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

        holder.send_tech_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(context).isOnline()) {

                    dialog_bar.show();
                    if (assign_Type.equals("ASSIGN")) {

                        choose_DATE(tech_id);

                    } else {
                        Choose_technical(tech_id);
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

        Button send_tech_btn;

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
            send_tech_btn = view.findViewById(R.id.send_tech_btn);


        }

    }


    private void choose_DATE(final String chosen_tech) {

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                date_assign = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                Log.d("tssst", date_assign);

                Choose_technical(chosen_tech);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            // Do Stuff
                            dialog_bar.dismiss();
                            dialog.dismiss();
                        }
                    }
                });

        //  fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);

        fromDatePickerDialog.show();

    }


    private static String date_assign;


    private void Choose_technical(final String tech_id) {

        String sub_URL = null;
        if (assign_Type.equals("ASSIGN")) {

            sub_URL = "sp/AcceptOrderAndAssignTech";

        } else if (assign_Type.equals("CHANGE")) {

            sub_URL = "sp/changeAssignedTech";

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + sub_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("respoooooo", response);

                        try {

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {
                                dialog_bar.dismiss();
                                context.finish();

                            } else {

                                StringBuilder s = new StringBuilder(100);
                                String street;
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

                params.put("order_id", order_id);
                params.put("tech_id", tech_id);


                if (assign_Type.equals("ASSIGN")) {

                    params.put("date", date_assign);
                }


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


}