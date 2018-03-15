package com.redray.khadoomhome.all_users.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.redray.khadoomhome.MainActivity;
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


public class Change_Password extends AppCompatActivity {


    RelativeLayout main_chang_pass_lay;

    EditText old_pass_edt , new_pass_edt , confirm_pass_edt;
    String old_pass_str , new_pass_str , confirm_pass_str;

    Button send_chang_pass_btn;

    KProgressHUD dialog_bar;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        main_chang_pass_lay = findViewById(R.id.main_chang_pass_lay);
        old_pass_edt = findViewById(R.id.old_password);
        new_pass_edt = findViewById(R.id.pass_new);
        confirm_pass_edt = findViewById(R.id.confirm_pass);


        send_chang_pass_btn = findViewById(R.id.save_chang_pass);
        send_chang_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        send_chang_pass();

                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(main_chang_pass_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            });

                    snackbar.show();
                }

            }
        });

    }


    public boolean validate() {
        boolean valid = true;

        try {
            // required
            old_pass_str = old_pass_edt.getText().toString().trim();
            new_pass_str = new_pass_edt.getText().toString().trim();
            confirm_pass_str = confirm_pass_edt.getText().toString().trim();

        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }



        if (TextUtils.isEmpty(old_pass_str) ||!Utility.passLength(old_pass_str)) {
            old_pass_edt.setError(getString(R.string.valid_pass));
            valid = false;
        } else {
            old_pass_edt.setError(null);

        }

        if (TextUtils.isEmpty(new_pass_str) ||!Utility.passLength(new_pass_str)) {
            new_pass_edt.setError(getString(R.string.valid_pass));
            valid = false;
        } else {
            new_pass_edt.setError(null);

        }


        if (!Utility.isMatching(new_pass_str, confirm_pass_str)) {
            confirm_pass_edt.setError(getString(R.string.valid_confirm));
            valid = false;
        } else {
            confirm_pass_edt.setError(null);

        }



        return valid;
    }




    public void send_chang_pass() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "ChangePassword",
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

                                Intent go_to_login = new Intent(getApplicationContext(),MainActivity.class);
                                go_to_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(go_to_login);
                                finish();

                                Toast.makeText(getApplicationContext(), R.string.pass_chng_succ, Toast.LENGTH_LONG).show();


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

                                Utility.dialog_error(Change_Password.this, s.toString());

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

                params.put("userType", sessionManager.getUser_Type());
                params.put("oldPassword", old_pass_str);
                params.put("newPassword", new_pass_str);

                Log.e("Params", params.toString());

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
