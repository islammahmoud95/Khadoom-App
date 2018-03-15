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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.AppController;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Change_Country_Activity extends AppCompatActivity {


    KProgressHUD dialog_bar;
    SessionManager sessionManager;

    RelativeLayout main_country_lay;


    private ArrayList<String> country_array;
    private ArrayList<String> id_country;

    private ArrayList<String> city_array;
    private ArrayList<String> id_city;

    private Spinner spinner_count;
    private Spinner  spinner_city;
    // android.app.AlertDialog dialog;


    String country_id_pos;
    String city_id_pos;


    Button change_country_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_country);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        sessionManager = new SessionManager(getApplicationContext());

        dialog_bar =  KProgressHUD.create(Change_Country_Activity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        //Initializing the ArrayList
        country_array = new ArrayList<>();
        id_country = new ArrayList<>();

        city_array = new ArrayList<>();
        id_city = new ArrayList<>();

        spinner_count = findViewById(R.id.spinner_country);
        spinner_city = findViewById(R.id.spinner_city);


        main_country_lay = findViewById(R.id.main_country_lay);

        change_country_btn = findViewById(R.id.change_country);
        change_country_btn.setEnabled(false);
        change_country_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_bar.show();
                change_country_city();


            }
        });

        if (Check_Con.getInstance(this).isOnline()) {

            get_country();
            dialog_bar.show();


            spinner_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("asfsaf",id_country.get(position));

                    country_id_pos = id_country.get(position);

                    get_city(id_country.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




            spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("asfsaf",country_id_pos);

                    city_id_pos = id_city.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        } else {


            country_array.add(getString(R.string.no_connect));
            city_array.add(getString(R.string.no_connect));


            spinner_count.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, country_array));

            spinner_city.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, city_array));


            Snackbar snackbar = Snackbar.make(main_country_lay, getString(R.string.no_connect), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.connect_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }

    }









    private void get_country(){
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL+"locations/countries",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob= new JSONObject(response);

                            JSONArray jsonarray = main_ob.getJSONArray("Data");

                            Log.d("tessst",response);
                            //Parsing the fetched Json String to JSON Object
                            //  JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                //   String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");

                                id_country.add(id);
                            }


                            parse_getCountry(jsonarray);

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
                          Toast.makeText(getApplicationContext(), R.string.server_down,Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("lang", sessionManager.getLang());

                Log.e("parms", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void parse_getCountry(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                country_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Setting adapter to show the items in the spinner

//        if (country_array.isEmpty())
//        {
//            country_array.add("No Countries");
//        }
        spinner_count.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, country_array));

        //  dialog.dismiss();
    }




    private void get_city(final String Country_id){
        //Creating a string request

        String tag_json_obj = "json_obj_city";

        //  dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL+"locations/cities",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        city_array.clear();
                        id_city.clear();

                        try {


                            JSONObject main_ob= new JSONObject(response);

                            JSONArray jsonarray = main_ob.getJSONArray("Data");

                            Log.d("tessst",response);
                            //Parsing the fetched Json String to JSON Object
                            //    JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                //   String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");
                                id_city.add(id);
                                Log.d("safsafas",id_city.toString());
                            }




                            parse_getCities(jsonarray);

                            dialog_bar.dismiss();
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
                          Toast.makeText(getApplicationContext(), R.string.server_down,Toast.LENGTH_LONG).show();
                        Log.e("error", error.getMessage() + "");

                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("country_id", Country_id);
                params.put("lang", sessionManager.getLang());

                Log.e("parm",params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void parse_getCities(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                city_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Setting adapter to show the items in the spinner
        spinner_city.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, city_array));

        // enable button again
        change_country_btn.setEnabled(true);

    }




    private void change_country_city(){
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL+"users/UserChangeCityApi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                dialog_bar.dismiss();
                                finish();

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

                                Utility.dialog_error(Change_Country_Activity.this, s.toString());

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
                          Toast.makeText(getApplicationContext(), R.string.server_down,Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("new_city_id", city_id_pos);

                Log.e("parms", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
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
