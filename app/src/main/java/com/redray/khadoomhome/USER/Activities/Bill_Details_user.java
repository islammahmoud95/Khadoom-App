package com.redray.khadoomhome.USER.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Bill_Details_user extends AppCompatActivity {

    KProgressHUD dialog_bar;
    SessionManager sessionManager;

    ScrollView scroll_bill_lay;


    EditText num_order_edt, bill_num_edt, visit_fees_edt, parts_fees_edt, addit_fees_edt;
    TextView total_bill;
    String bill_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details_user);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent !=null){
            bill_ID = intent.getStringExtra("BILL_ID");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        scroll_bill_lay = findViewById(R.id.scroll_bill_lay);

        num_order_edt = findViewById(R.id.num_order_edt);
        bill_num_edt = findViewById(R.id.bill_num_edt);
        visit_fees_edt = findViewById(R.id.visit_fees_edt);
        parts_fees_edt = findViewById(R.id.parts_fees_edt);
        addit_fees_edt = findViewById(R.id.addit_fees_edt);


        total_bill = findViewById(R.id.total_bill);

        Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            dialog_bar.show();

            get_Bill_Details();


        } else {


            Snackbar snackbar = Snackbar
                    .make(scroll_bill_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }

    }


    public void get_Bill_Details() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/getInvoiceDetails",
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
                                String num_order_str = stat_reg.getString("order_id");
                                String bill_num_str = stat_reg.getString("id");
                                String visit_fees_str = stat_reg.getString("visit_fees");
                                String parts_fees_str = stat_reg.getString("parts_fees");
                                String addit_fees_str = stat_reg.getString("additional_work_fees");

                                String total_bill_str = stat_reg.getString("total");

                                num_order_edt.setText(num_order_str);
                                bill_num_edt.setText(bill_num_str);
                                visit_fees_edt.setText(visit_fees_str);
                                parts_fees_edt.setText(parts_fees_str);
                                addit_fees_edt.setText(addit_fees_str);

                                total_bill.setText(getString(R.string.total) + " " + total_bill_str);

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

                                dialog_bar.dismiss();

                                Utility.dialog_error(Bill_Details_user.this, s.toString());

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
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

             //   params.put("lang", sessionManager.getLang());
                params.put("order_id", bill_ID);

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


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(stringRequest);
    }


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