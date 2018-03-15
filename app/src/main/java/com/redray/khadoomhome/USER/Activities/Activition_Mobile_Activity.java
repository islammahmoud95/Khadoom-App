package com.redray.khadoomhome.USER.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.redray.khadoomhome.all_users.Activites.Login_Activity;
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

public class Activition_Mobile_Activity extends AppCompatActivity {

    public static boolean active = false;
    Context context;
    BroadcastReceiver updateUIReciver;

    Intent intent;
    String reg_ID;

    KProgressHUD dialog_bar;

    EditText active_mobile_edt;
    String active_mobile_str;

    Button actvit_account_btn;

    LinearLayout active_main;

    TextView resend_activition_clk;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activition_mobile);

        intent = getIntent();
        //MessageID is the location of the messages for this specific chat
        reg_ID = intent.getStringExtra("REG_ID");
        Log.d("id", reg_ID);

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        active_main = findViewById(R.id.active_main);
        active_mobile_edt = findViewById(R.id.active_mobile_edt);
        actvit_account_btn = findViewById(R.id.actvit_account_btn);
        actvit_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        active_mob();

                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(active_main, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


        resend_activition_clk = findViewById(R.id.resend_activition_clk);
        resend_activition_clk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    dialog_bar.show();
                    resend_Active_Mob();

                } else {


                    Snackbar snackbar = Snackbar.make(active_main, getString(R.string.no_connect), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.connect_snackbar), new View.OnClickListener() {
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

    // this bloack for auto active regiser

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    // for auto verify
    @Override
    protected void onResume() {
        super.onResume();

        context = this;

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.redray.khadoomhome.USER.Activities.Activition_Mobile_Activity");
//        updateUIReciver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //UI update here
//                if (intent != null) {
//                    Toast.makeText(context, intent.getStringExtra("Active_Num"), Toast.LENGTH_LONG).show();
//                    Log.d("sfsafsafsaf",intent.getStringExtra("Active_Num"));
//                    active_mobile_edt.setText(intent.getStringExtra("Active_Num"));
//                }
//            }
//        };
//        registerReceiver(updateUIReciver, filter);
    }




    public boolean validate() {
        boolean valid = true;

        try {

            // spinners , locations

            // required
            active_mobile_str = active_mobile_edt.getText().toString().trim();


        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (active_mobile_str.isEmpty() || active_mobile_str.length() != 6) {
            active_mobile_edt.setError(getString(R.string.valid_activition));

            valid = false;
        } else {
            active_mobile_edt.setError(null);

        }

        return valid;
    }

    public void active_mob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/ActiveUser",
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


                                Intent go_to_Login = new Intent(getApplicationContext(),Login_Activity.class);
                                go_to_Login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(go_to_Login);


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
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", reg_ID);
                params.put("code", active_mobile_str);

                Log.e("Params", params.toString());

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(stringRequest);
    }


    public void resend_Active_Mob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/resend_code",
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


                                Toast.makeText(getApplicationContext(), R.string.activition_mobile_sent,Toast.LENGTH_LONG).show();

//                                Intent go_to_Login = new Intent(getApplicationContext(),Login_Activity.class);
//                                startActivity(go_to_Login);

//                                JSONObject stat_reg = obj.getJSONObject("Data");
//                                String id_user = stat_reg.getString("id");
//
//                                Intent activ_reg = new Intent(getApplicationContext(), Activition_Mobile_Activity.class);
//                                activ_reg.putExtra("REG_ID", id_user);
//                                startActivity(activ_reg);
//
//                                Log.d("done", id_user);


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
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                params.put("id", reg_ID);
                params.put("userType", sessionManager.getUser_Type());

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

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
