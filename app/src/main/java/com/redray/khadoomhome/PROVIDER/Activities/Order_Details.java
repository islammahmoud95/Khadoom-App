package com.redray.khadoomhome.PROVIDER.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.PROVIDER.Adapters.Get_Parts_view_adapter;
import com.redray.khadoomhome.PROVIDER.Models.Get_Parts_view_Items;
import com.redray.khadoomhome.PROVIDER.Parser.ParseGet_Parts_view;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.ImageLoader.ImageGesture;
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

public class Order_Details extends AppCompatActivity implements OnMapReadyCallback {

    KProgressHUD dialog_bar;
    SessionManager sessionManager;

    LinearLayout main_prov_detail_lay;

    private GoogleMap mMap;

    String order_id = "";


    TextView title_order_txt,service_txt,date_txt,address_txt , username_txt , user_phone_txt;
    Button refused_btn , accept_btn;

    ImageView imageView1 , imageView2;

    LinearLayout parts_lay_view;
    LinearLayout images_row_lay;

    RecyclerView recyclerView_get_parts;
    LinearLayoutManager manager;

    Get_Parts_view_adapter productsAdapter_Parts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_prov);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_contact);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("ORDER_ID");
        }


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        main_prov_detail_lay = findViewById(R.id.main_prov_detail_lay);

        parts_lay_view = findViewById(R.id.parts_lay_view);

        images_row_lay = findViewById(R.id.images_row_lay);

        title_order_txt = findViewById(R.id.title_order);

        username_txt = findViewById(R.id.username_txt);

        user_phone_txt = findViewById(R.id.user_phone_txt);


        service_txt = findViewById(R.id.service_txt);
        date_txt = findViewById(R.id.date_txt);
        address_txt = findViewById(R.id.address_txt);

        refused_btn = findViewById(R.id.refused_btn);
        accept_btn = findViewById(R.id.accept_btn);

        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);


        recyclerView_get_parts = findViewById(R.id.get_parts_recycle);
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





        refused_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // this validation cause if id is null that's mean
                // no internet or webservice error
                if (!order_id.equals("")) {


                    showRefusedDialog();


                } else {


                    Snackbar snackbar = Snackbar
                            .make(main_prov_detail_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                    // here workaround we put key to make different between
                    // change technical
                    // and accept order and assign it to technical
                    //Assign_Type  ASSIGN for accepted
                    //Assign_Type change

                    Intent choose_technical = new Intent(getApplicationContext(),Choose_Technical.class);
                    choose_technical.putExtra("ORDER_ID",order_id);
                    choose_technical.putExtra("Assign_Type","ASSIGN");
                    startActivity(choose_technical);


                } else {


                    Snackbar snackbar = Snackbar
                            .make(main_prov_detail_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        mMap = googleMap;




    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            dialog_bar.show();

            get_order_details();


        } else {


            Snackbar snackbar = Snackbar
                    .make(main_prov_detail_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }

    }


    public void get_order_details() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/getOrderDetails",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                JSONObject data = obj.getJSONObject("Data");

                            //    String id_str = data.getString("id");

                                String desc_str = data.getString("desc");
                                title_order_txt.setText(desc_str);

                                String req_type_str = data.getString("req_type");
                                if (req_type_str.equals("A")|| req_type_str.equals("D") )
                                {
                                    accept_btn.setVisibility(View.GONE);
                                    refused_btn.setVisibility(View.GONE);
                                }

                                String address_str = data.getString("address");
                                address_txt.setText(address_str);

                                String lat_str = data.getString("lat");
                                String lng_str = data.getString("lng");


                                LatLng TutorialsPoint = new LatLng(Double.valueOf(lat_str), Double.valueOf(lng_str));

                                BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.marker);

                                mMap.addMarker(new MarkerOptions()
                                        .snippet(getString(R.string.visit_us))
                                        .position(TutorialsPoint)
                                        .icon(map_client)
                                        .title(getString(R.string.comp_loc))
                                        .flat(true)
                                        .draggable(true));


                                CameraPosition cameraPosition = new CameraPosition.Builder().target(TutorialsPoint) // Sets the center
                                        // of the map to
                                        // location user
                                        .zoom(15) // Sets the zoom
                                        .build(); // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                String username_str = data.getString("user_name");
                                username_txt.setText(username_str);


                                String user_phone_str = data.getString("user_phone");
                                user_phone_txt.setText(user_phone_str);


                                String sub_cat_str = data.getString("sub_cat_str");
                                service_txt.setText(sub_cat_str);

                                String additional_parts_str = data.getString("additional_parts");
                                if (additional_parts_str.equals("1")) // that's mean yes chosen
                                {

                                    showProducts_view(String.valueOf(data));

                                    parts_lay_view.setVisibility(View.VISIBLE);



                                } else {

                                    parts_lay_view.setVisibility(View.GONE);

                                }

                                String date_str = data.getString("date");
                                date_txt.setText(date_str);


                                JSONArray images_array = data.getJSONArray("images");


                                if (images_array !=null) {


                                    if(images_array.length() == 0) {


                                        images_row_lay.setVisibility(View.GONE);

                                    }else if (images_array.length() == 1) {


                                        final String img1_url = images_array.getJSONObject(0).getString("url");
                                       // String img1_id = images_array.getJSONObject(0).getString("id");


                                        Glide.with(getApplicationContext())
                                                .load(img1_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(imageView1);

                                     //   order_image.setTag(img1_id);


                                        imageView1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent i = new Intent(getApplicationContext(), ImageGesture.class);
                                                i.putExtra("URL_OF_IMAGE", img1_url);
                                                Log.d("URL_OF_IMAGE",img1_url);
                                                startActivity(i);
                                            }
                                        });

                                        imageView2.setVisibility(View.GONE);

                                    }else if (images_array.length() == 2){


                                        final String img1_url = images_array.getJSONObject(0).getString("url");


                                        Glide.with(getApplicationContext())
                                                .load(img1_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(imageView1);



                                        final String img2_url = images_array.getJSONObject(1).getString("url");


                                        Glide.with(getApplicationContext())
                                                .load(img2_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(imageView2);

                                     //   order_image2.setTag(img2_id);


                                        imageView1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent i = new Intent(getApplicationContext(), ImageGesture.class);
                                                i.putExtra("URL_OF_IMAGE", img1_url);
                                                Log.d("URL_OF_IMAGE",img1_url);
                                                startActivity(i);
                                            }
                                        });

                                        imageView2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent i = new Intent(getApplicationContext(), ImageGesture.class);
                                                i.putExtra("URL_OF_IMAGE", img2_url);
                                                Log.d("URL_OF_IMAGE",img2_url);
                                                startActivity(i);
                                            }
                                        });

                                    }

                                }



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

                                Utility.dialog_error(Order_Details.this, s.toString());

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

                params.put("order_id", order_id);

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



    private void showProducts_view(String json) {

        ParseGet_Parts_view parseProducts;
        parseProducts = new ParseGet_Parts_view(json);

        productsArrayList_Parts = parseProducts.parseProducts();

        //    if (!productsArrayList_Parts.isEmpty()) {

        productsAdapter_Parts = new Get_Parts_view_adapter(this, productsArrayList_Parts);

        recyclerView_get_parts.setAdapter(productsAdapter_Parts);

        //   } else {
        productsAdapter_Parts.notifyDataSetChanged();
        //   }

    }
    ArrayList<Get_Parts_view_Items> productsArrayList_Parts = new ArrayList<>();






    Dialog dialog;
    EditText refuse_reason_edt;
    String refuse_reson;

    private void showRefusedDialog() {
        dialog = new Dialog(Order_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_refuse_order);
        }

        refuse_reason_edt =  dialog.findViewById(R.id.reason_ref_edt);


        Button send_reson =  dialog.findViewById(R.id.send_btn);
        send_reson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        send_Refused_order();

                    }

                } else {

                    Snackbar snackbar = Snackbar
                            .make(main_prov_detail_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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

        Button cancel_btn =  dialog.findViewById(R.id.cancel_reason_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public boolean validate() {
        boolean valid = true;

        try {


            // required
            refuse_reson = refuse_reason_edt.getText().toString().trim();



        }catch (Exception e){

            Log.e("test",e.getMessage());
        }


        if (refuse_reson.isEmpty()) {
            refuse_reason_edt.setError(getString(R.string.valid_ref_reason));
            valid = false;
        } else {
            refuse_reason_edt.setError(null);

        }





        return valid;
    }



    public void send_Refused_order() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/refusedOrders",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {



                                Intent go_to_SERVICES = new Intent(getApplicationContext(),MainActivity.class);
                                go_to_SERVICES.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(go_to_SERVICES);
                                dialog_bar.dismiss();
                                dialog.dismiss();
                                finish();

                                Toast.makeText(getApplicationContext(),"Order Refused Successfully",Toast.LENGTH_LONG).show();

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

                                Utility.dialog_error(Order_Details.this, s.toString());

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

                params.put("order_id", order_id);
                params.put("reason", refuse_reson);
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
