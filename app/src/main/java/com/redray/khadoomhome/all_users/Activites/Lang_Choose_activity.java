package com.redray.khadoomhome.all_users.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.AppController;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Lang_Choose_activity extends AppCompatActivity {

    SessionManager sessionManager;

    ImageButton en_btn, ar_btn;

    KProgressHUD dialog_bar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_choose);

        sessionManager = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        en_btn = findViewById(R.id.en_lang_btn);
        ar_btn = findViewById(R.id.ar_lang_btn);


        ar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ar_btn.setBackground(ContextCompat.getDrawable(Lang_Choose_activity.this, R.drawable.arabic_lang_selected));

                en_btn.setBackground(ContextCompat.getDrawable(Lang_Choose_activity.this, R.drawable.english_lang_unselected));


                setLocale("ar");



            }
        });


        en_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                en_btn.setBackground(ContextCompat.getDrawable(Lang_Choose_activity.this, R.drawable.english_lang_selected));

                ar_btn.setBackground(ContextCompat.getDrawable(Lang_Choose_activity.this, R.drawable.arabic_lang_unselected));

                setLocale("en");

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (sessionManager.getLang().equals("en")) {
            en_btn.setPressed(true);
            en_btn.setSelected(true);

        } else {

            ar_btn.setPressed(true);
            ar_btn.setSelected(true);
        }

    }

    public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        sessionManager.set_user_lang(lang);


        if (sessionManager.isLoggedIn())
        {
            dialog_bar.show();
            change_Lang();

        }else {
                Intent refresh = new Intent(this, Account_Type.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(refresh);
            }
    }




    private void change_Lang() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "changeLanguage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                dialog_bar.dismiss();

                                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(refresh);

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

                                Utility.dialog_error(Lang_Choose_activity.this, s.toString());

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
                params.put("lang", sessionManager.getLang());
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

                if (headers == null || headers.equals(Collections.emptyMap())) {
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

}
