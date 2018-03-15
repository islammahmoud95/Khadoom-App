package com.redray.khadoomhome.USER.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Activities.Edit_Order_Activity;
import com.redray.khadoomhome.USER.Activities.Order_View_Details;
import com.redray.khadoomhome.USER.Models.Get_Requests_Items;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Get_My_Requests extends RecyclerView.Adapter<Get_My_Requests.RecyclerViewHolder> {

    private Activity context;

    private ArrayList<Get_Requests_Items> productsArrayList;


    private SessionManager sessionManager;


    private KProgressHUD dialog_bar;

    public Get_My_Requests(Activity context, ArrayList<Get_Requests_Items> productsArrayList) {

        this.context = context;
        this.productsArrayList = productsArrayList;

    }

    public void updateList(ArrayList<Get_Requests_Items> list) {
        productsArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requests, parent, false);

        dialog_bar = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        return new RecyclerViewHolder(view, context, productsArrayList);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //   final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        final int pos = position;


        sessionManager = new SessionManager(context);


        final String order_id = productsArrayList.get(position).getrequest_Id();

        Log.d("iddd", order_id);


        String name_str = productsArrayList.get(position).getRequest_Name();
        holder.subject_name.setText(name_str);


        String main_serv_str = productsArrayList.get(position).getMain_service();
        holder.subject_main_serv.setText(main_serv_str);


        String date_str = productsArrayList.get(position).getDate();
        holder.subject_date.setText(date_str);


        String stats_str = productsArrayList.get(position).getRequest_Status();
        final String req_type = productsArrayList.get(position).getRequest_Type();

        holder.subject_status_txt.setText(stats_str);


        switch (req_type) {
            case "P":
                //pending yelllow
                holder.subject_status_txt.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                holder.subject_status_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pendding_icon));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        edit_service(order_id, pos);

                    }
                });


                break;
            case "C":
                //  cancel red
                holder.subject_status_txt.setTextColor(Color.RED);
                holder.subject_status_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cancel_icon));
                holder.itemView.setOnClickListener(null);

                break;
            case "D":
                //  done gerrn
                holder.subject_status_txt.setTextColor(Color.GREEN);
                holder.subject_status_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.done_icon));

                break;
            case "A":
                //  accepted blue
                holder.subject_status_txt.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
                holder.subject_status_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.accepted_icon));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent go_to_detail = new Intent(context, Order_View_Details.class);
                        go_to_detail.putExtra("ORDER_ID", order_id);
                        context.startActivity(go_to_detail);

                    }
                });

                break;

            default:
                break;
        }


    }


    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView subject_name;
        TextView subject_main_serv;
        TextView subject_date;
        ImageView subject_status_img;
        TextView subject_status_txt;


        Context context;
        ArrayList<Get_Requests_Items> productsArrayList;


        RecyclerViewHolder(View view, Context context, final ArrayList<Get_Requests_Items> productsArrayList) {

            super(view);

            this.context = context;
            this.productsArrayList = productsArrayList;


            subject_name = view.findViewById(R.id.name_req);


            subject_main_serv = view.findViewById(R.id.main_serv_req);
            subject_date = view.findViewById(R.id.date_req);
            subject_status_img = view.findViewById(R.id.status_img_req);
            subject_status_txt = view.findViewById(R.id.status_txt);


            // subject_name =  view.findViewById(R.id.orders_txt);


        }

    }

    private Dialog dialog;
    private void edit_service(final String order_ID , final int pos) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_add_services);
        }

        TextView content_txt =  dialog.findViewById(R.id.reg_terms_tv);
        content_txt.setText(R.string.request_edit_cancel_msg);

        Button add_now =  dialog.findViewById(R.id.now_btn);
        add_now.setText(R.string.edit);
        add_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_SERVICES = new Intent(context, Edit_Order_Activity.class);
                go_to_SERVICES.putExtra("ORDER_ID", order_ID);
                context.startActivity(go_to_SERVICES);
                dialog.dismiss();

            }
        });

        Button add_later =  dialog.findViewById(R.id.later_btn);
        add_later.setText(context.getString(R.string.cancel));
        //  mRegister.setTypeface(mBaseTextFont);
        add_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                cancel_order(order_ID,pos);
                dialog_bar.show();

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void cancel_order(final String order_ID, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/cancelOrder",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {
                                dialog_bar.dismiss();

                                productsArrayList.get(position).setRequest_Type("C");
                                productsArrayList.get(position).setRequest_Status("CANCELED");
                                notifyDataSetChanged();

                                Toast.makeText(context, R.string.order_cancl_suc, Toast.LENGTH_LONG).show();



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

                                //  Utility.dialog_error(Activition_Mobile_Activity.this, s.toString());

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

                params.put("order_id", order_ID);


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

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
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