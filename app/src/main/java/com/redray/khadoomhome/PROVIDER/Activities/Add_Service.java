package com.redray.khadoomhome.PROVIDER.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.PROVIDER.Adapters.Add_service_category;
import com.redray.khadoomhome.PROVIDER.Adapters.Add_service_cities;
import com.redray.khadoomhome.PROVIDER.DataTransferInterface_Add_Serv;
import com.redray.khadoomhome.PROVIDER.Models.Add_service_items;
import com.redray.khadoomhome.PROVIDER.Parser.Add_Service_Parser;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.all_users.Activites.Login_Activity;
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


public class Add_Service extends AppCompatActivity implements DataTransferInterface_Add_Serv {


    //this is country city
    private ArrayList<String> country_array;
    private ArrayList<String> id_country;
    private Spinner spinner_count;
    String country_id_pos = "";
    RecyclerView recyclerView_city;
    Add_service_cities productsAdapter_cities;


    //this is services
    private ArrayList<String> main_servics_array;
    private ArrayList<String> id_main_serv;
    private Spinner spinner_main_serv;
    String main_serv_id_pos = "";
    RecyclerView recyclerView_services;
    Add_service_category productsAdapter_services;


    LinearLayout main_add_ser_lay;
    LinearLayoutManager manager;
    GridLayoutManager manager2;

    KProgressHUD dialog_bar;

    SessionManager session;

    String reg_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        if add service before login
        Intent intent = getIntent();
        if (intent != null) {
            reg_id = intent.getStringExtra("REG_ID");
        }


        session = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(Add_Service.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        //Initializing the ArrayList
        main_servics_array = new ArrayList<>();
        id_main_serv = new ArrayList<>();
        spinner_main_serv = findViewById(R.id.spinner_main_service);


        recyclerView_services = findViewById(R.id.recycle_view_sub_serv);


        manager2 = new GridLayoutManager(this, 2);

        recyclerView_services.setLayoutManager(manager2);
        recyclerView_services.setAdapter(new RecyclerView.Adapter() {
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


        recyclerView_services.setHasFixedSize(true);
        recyclerView_services.setNestedScrollingEnabled(false);
//


        //Initializing the ArrayList
        country_array = new ArrayList<>();
        id_country = new ArrayList<>();

        spinner_count = findViewById(R.id.spinner_country);


        recyclerView_city = findViewById(R.id.recycle_view_city);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_city.setLayoutManager(manager);
        recyclerView_city.setAdapter(new RecyclerView.Adapter() {
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


        recyclerView_city.setHasFixedSize(true);
        recyclerView_city.setNestedScrollingEnabled(false);


        if (Check_Con.getInstance(this).isOnline()) {

            get_Main_Serv();

            get_country();


            spinner_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here

                    Log.d("asfsaf", id_country.get(position));

                    country_id_pos = id_country.get(position);

                    get_city(id_country.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            spinner_main_serv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here

                    Log.d("asfsaf", id_main_serv.get(position));

                    main_serv_id_pos = id_main_serv.get(position);

                    get_sub_serv(id_main_serv.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


        } else {


            country_array.add("No Countries");
            //  city_array.add("No Cities");


            spinner_count.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item, country_array));


            main_servics_array.add("No Main service");
            //  city_array.add("No Cities");


            spinner_main_serv.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item, main_servics_array));

        }


        main_add_ser_lay = findViewById(R.id.main_add_ser_lay);

        Button add_serv_bn = findViewById(R.id.add_serv_bn);
        add_serv_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                    dialog_bar.show();
                    post_Service();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(main_add_ser_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


    public ArrayList<?> aray_SERVICES = new ArrayList<>();

    public ArrayList<?> aray_CITIES = new ArrayList<>();

    String aray_cities_str = "";
    String aray_services_str = "";

    @Override
    public void setValues_cities(ArrayList<?> al) {

        aray_CITIES = al;

        aray_cities_str = aray_CITIES.toString().substring(1, aray_CITIES.toString().length() - 1);


        Log.d("cities", aray_CITIES.toString());
    }

    @Override
    public void setValues_services(ArrayList<?> al) {

        aray_SERVICES = al;

        aray_services_str = aray_SERVICES.toString().substring(1, aray_SERVICES.toString().length() - 1);

        Log.d("sub-services", aray_SERVICES.toString());
    }


    private void get_country() {
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "locations/countries",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            JSONArray jsonarray = main_ob.getJSONArray("Data");

                            Log.d("tessst", response);
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
                        Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("lang", session.getLang());

                Log.e("parms", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void parse_getCountry(JSONArray j) {
        //Traversing through all the items in the json array

        for (int i = 0; i < j.length(); i++) {
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


    private void get_city(final String Country_id) {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "locations/cities",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        showProducts(response);

//                        city_array.clear();
//                        id_city.clear();
//
//                        try {
//
//
//                            JSONObject main_ob = new JSONObject(response);
//
//                            JSONArray jsonarray = main_ob.getJSONArray("Data");
//
//                            Log.d("tessst", response);
//                            //Parsing the fetched Json String to JSON Object
//                            //    JSONArray jsonarray = new JSONArray(response);
//
//                            for (int i = 0; i < jsonarray.length(); i++) {
//                                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                                String name = jsonobject.getString("name");
//                                String id = jsonobject.getString("id");
//                                //   id_city.add(id);
//                                //  Log.d("safsafas",id_city.toString());
//                            }
//
//
//                            //Calling method getStudents to get the students from the JSON Array
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   progressDialog.dismiss();


                        Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
//
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(), "erre" + "", Toast.LENGTH_LONG).show();
//
//
//                        } else if (error instanceof AuthFailureError) {
//                            //TODO
//                        } else if (error instanceof ServerError) {
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            //TODO
//                        } else if (error instanceof ParseError) {
//                            //TODO
//                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("country_id", Country_id);
                params.put("lang", session.getLang());


                // Log.e(TAG,params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }


    private void showProducts(String json) {

        Add_Service_Parser parseProducts;
        parseProducts = new Add_Service_Parser(json);

        productsArrayList_city = parseProducts.parseProducts();

//        if (!productsArrayList.isEmpty()) {


        productsAdapter_cities = new Add_service_cities(this, productsArrayList_city, this);


        recyclerView_city.setAdapter(productsAdapter_cities);
//            productsArrayList.clear();

        //   } else {
        productsAdapter_cities.notifyDataSetChanged();
        //     }

    }

    ArrayList<Add_service_items> productsArrayList_city = new ArrayList<>();


    private void get_Main_Serv() {
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "services/categories",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            JSONArray jsonarray = main_ob.getJSONArray("Data");

                            Log.d("tessst", response);
                            //Parsing the fetched Json String to JSON Object
                            //  JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                //   String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");

                                id_main_serv.add(id);
                            }


                            parse_getMain_Serv(jsonarray);

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
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("lang", session.getLang());

                Log.e("parms", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void parse_getMain_Serv(JSONArray j) {
        //Traversing through all the items in the json array

        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                main_servics_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Setting adapter to show the items in the spinner

//        if (country_array.isEmpty())
//        {
//            country_array.add("No Countries");
//        }
        spinner_main_serv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, main_servics_array));

        //  dialog.dismiss();
    }


    private void get_sub_serv(final String Country_id2) {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "services/subCategories",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        showSub_serv(response);


//                        city_array.clear();
//                        id_city.clear();
//
//                        try {
//
//
//                            JSONObject main_ob = new JSONObject(response);
//
//                            JSONArray jsonarray = main_ob.getJSONArray("Data");
//
//                            Log.d("tessst", response);
//                            //Parsing the fetched Json String to JSON Object
//                            //    JSONArray jsonarray = new JSONArray(response);
//
//                            for (int i = 0; i < jsonarray.length(); i++) {
//                                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                                String name = jsonobject.getString("name");
//                                String id = jsonobject.getString("id");
//                                //   id_city.add(id);
//                                //  Log.d("safsafas",id_city.toString());
//                            }
//
//
//
//
//                            //Calling method getStudents to get the students from the JSON Array
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   progressDialog.dismiss();


                        Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
//
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(), "erre" + "", Toast.LENGTH_LONG).show();
//
//
//                        } else if (error instanceof AuthFailureError) {
//                            //TODO
//                        } else if (error instanceof ServerError) {
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            //TODO
//                        } else if (error instanceof ParseError) {
//                            //TODO
//                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("category_id", Country_id2);
                params.put("lang", session.getLang());


                // Log.e(TAG,params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }


    private void showSub_serv(String json) {

        Add_Service_Parser parseProducts;
        parseProducts = new Add_Service_Parser(json);

        productsArrayList_service = parseProducts.parseProducts();


        productsAdapter_services = new Add_service_category(this, productsArrayList_service, this);

        recyclerView_services.setAdapter(productsAdapter_services);

        productsAdapter_services.notifyDataSetChanged();


    }

    ArrayList<Add_service_items> productsArrayList_service = new ArrayList<>();


    private void post_Service() {
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/AddServices",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {
                                dialog_bar.dismiss();

                                if (session.isLoggedIn()) {
                                    finish();

                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }


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

                                Utility.dialog_error(Add_Service.this, s.toString());

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
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("countries", country_id_pos);
                params.put("city_id", aray_cities_str);
                params.put("cats", main_serv_id_pos);
                params.put("subCats", aray_services_str);

                if (!session.isLoggedIn()) {
                    params.put("sp_id", reg_id);
                }

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
