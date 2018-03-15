package com.redray.khadoomhome.tickets.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.tickets.adapter.Get_My_Tickets_adapter;
import com.redray.khadoomhome.tickets.models.Get_Tickets_Items;
import com.redray.khadoomhome.tickets.parser.ParseGet_tickets;
import com.redray.khadoomhome.utilities.AppController;
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

public class Main_Get_Tickets extends AppCompatActivity {


    KProgressHUD dialog_bar;
    SessionManager sessionManager;


    RecyclerView recyclerView_get_parts;
    LinearLayoutManager manager;
    Get_My_Tickets_adapter productsAdapter_tickets;

    RelativeLayout main_ticket_lay;

    FloatingActionButton create_ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_get_tickets);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        main_ticket_lay = findViewById(R.id.main_ticket_lay);

        recyclerView_get_parts = findViewById(R.id.get_tickets_recycle);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_get_parts.setLayoutManager(manager);
        recyclerView_get_parts.setAdapter(new RecyclerView.Adapter() {
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


        recyclerView_get_parts.setHasFixedSize(true);
        recyclerView_get_parts.setNestedScrollingEnabled(true);

        create_ticket = findViewById(R.id.create_ticket);
        create_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent creat_ticket = new Intent(getApplicationContext(), Create_new_Ticket.class);
                startActivity(creat_ticket);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "1");



        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            get_Tickets();
            dialog_bar.show();


        }else {

            Snackbar snackbar = Snackbar
                    .make(main_ticket_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();

        }



    }



    private void get_Tickets() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "getAllTickets",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                showProducts(response);
                                dialog_bar.dismiss();

                            } else {

                                StringBuilder s = new StringBuilder(100);
                                String street ;
                                JSONArray st = main_ob.getJSONArray("Errors");
                                for (int i = 0; i < st.length(); i++) {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest", s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(Main_Get_Tickets.this, s.toString());

                            }


                            //Calling method getStudents to get the students from the JSON Array

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog_bar.dismiss();

                        Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();

                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("userType", sessionManager.getUser_Type());
                Log.e("parms", params.toString());

                return params;
            }

            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login",responseHeaders.get("Set-Cookie"));
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


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void showProducts(String json) {

        ParseGet_tickets parseProducts;
        parseProducts = new ParseGet_tickets(json);

        productsArrayList_Parts = parseProducts.parseProducts();

     //   if (!productsArrayList_Parts.isEmpty()) {

            productsAdapter_tickets = new Get_My_Tickets_adapter(this, productsArrayList_Parts);

            recyclerView_get_parts.setAdapter(productsAdapter_tickets);

      //  } else {
            productsAdapter_tickets.notifyDataSetChanged();
    //    }

    }

    ArrayList<Get_Tickets_Items> productsArrayList_Parts = new ArrayList<>();



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
