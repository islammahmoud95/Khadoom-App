package com.redray.khadoomhome.USER.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.redray.khadoomhome.USER.Adapters.Orders_Sub_adapter;
import com.redray.khadoomhome.USER.Models.My_Orders_Sub_Items;
import com.redray.khadoomhome.USER.Parser.ParseMy_Orders_Sub;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Orders_Sub_Activity extends AppCompatActivity {


    RecyclerView recyclerView;
    GridLayoutManager manager;

    KProgressHUD dialog_bar;

    Orders_Sub_adapter productsAdapter;

    SessionManager session;


    String main_order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_sub);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent main_intent;main_intent = getIntent();
        if (main_intent !=null)
        {
            main_order_id =  main_intent.getStringExtra("MAIN_SERVICE_ID");
            Log.d("iddddddd",main_order_id);
        }

        session = new SessionManager(getApplicationContext());

        dialog_bar =  KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);



        manager = new GridLayoutManager(getApplicationContext() , 3);

        recyclerView = findViewById(R.id.orders_recycle);



        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "1");



        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            Get_My_list_posts();
            dialog_bar.show();


        }else {

            Snackbar snackbar = Snackbar
                    .make(recyclerView, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();

        }

    }


    public void Get_My_list_posts() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/subServicesBasedUserCity",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("respoooooo", response);

                        try {


                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status)
                            {
                                dialog_bar.dismiss();


                                showProducts(response);



                            }else
                            {

                                StringBuilder s = new StringBuilder(100);
                                String street;
                                JSONArray st = obj.getJSONArray("Errors");
                                for(int i=0;i<st.length();i++)
                                {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest",s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(Orders_Sub_Activity.this,s.toString());

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

                        Toast.makeText(getApplicationContext(), R.string.server_down,Toast.LENGTH_LONG).show();

                        Log.e("error",error.getMessage() + "");
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("lang",session.getLang());
                params.put("category_id",main_order_id);

                Log.d("ProductsParams", params.toString());

                return params;
            }


            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login",responseHeaders.get("Set-Cookie"));
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


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private void showProducts(String json) {

        ParseMy_Orders_Sub parseProducts;
        parseProducts = new ParseMy_Orders_Sub(json);

        productsArrayList= parseProducts.parseProducts();

//        if (!productsArrayList.isEmpty()) {
        productsAdapter = new Orders_Sub_adapter(this, productsArrayList);
        recyclerView.setAdapter(productsAdapter);
//            productsArrayList.clear();

        //   } else {
        productsAdapter.notifyDataSetChanged();
        //     }

    }

    ArrayList<My_Orders_Sub_Items> productsArrayList = new ArrayList<>();




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
