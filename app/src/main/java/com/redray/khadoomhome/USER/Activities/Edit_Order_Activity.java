package com.redray.khadoomhome.USER.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Adapters.Get_Parts_adapter;
import com.redray.khadoomhome.USER.Models.Get_Parts_Items;
import com.redray.khadoomhome.USER.Parser.ParseGet_Parts;
import com.redray.khadoomhome.USER.Parser.ParseGet_Parts_EDIT;
import com.redray.khadoomhome.all_users.Activites.MapActivity;
import com.redray.khadoomhome.utilities.AppController;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Edit_Order_Activity extends AppCompatActivity {


    KProgressHUD dialog_bar;


    String sub_ser_id = "";
    String order_id = "";

    RecyclerView recyclerView_get_parts;
    LinearLayoutManager manager;
    LinearLayout main_make_order_lay;
    Get_Parts_adapter productsAdapter_Parts;


    Button edit_Order_btn;

    SessionManager sessionManager;


    JSONArray jsonArray_parts;


    //order type
    EditText desc_edt, locations_edt, address_edt;

    static TextView pref_date_edt;

    String desc_str = "",
            pref_date_str = "",
            order_Type_Str = "",
            order_additinal_Str = "",
            address_str = "";

    ScrollView scroll_view_order;


    RadioGroup radioGroup_order_Type, radioGroup_additional_parts;

    LinearLayout parts_lay;


    private String mLatitude = null;
    private String mLongitude = null;

    ImageButton location_btn;

    int btn_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle("Edit Order");
        }


        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra("ORDER_ID");
        }

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());



        address_edt = findViewById(R.id.address_order);
        address_edt.setText(sessionManager.getUserDetails().get("Address"));

        mLatitude = sessionManager.getUserDetails().get("LATITUDE");
        mLongitude = sessionManager.getUserDetails().get("LONGITUDE");


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


        scroll_view_order = findViewById(R.id.scroll_view_order);
        main_make_order_lay = findViewById(R.id.main_make_order_lay);

        desc_edt = findViewById(R.id.desc_edt);


        pref_date_edt = findViewById(R.id.pref_date_order);
        pref_date_edt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DialogFragment dialogfragment = new Edit_Order_Activity.DatePickerDialogTheme1();

                dialogfragment.show(getFragmentManager(), "Theme 1");

            }

        });


        parts_lay = findViewById(R.id.parts_lay);

        radioGroup_order_Type = findViewById(R.id.radioGroup_order_Type);


        radioGroup_additional_parts = findViewById(R.id.radioGroup_additional_parts);
        radioGroup_additional_parts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                View radioButton = radioGroup_additional_parts.findViewById(checkedId);
                int index = radioGroup_additional_parts.indexOfChild(radioButton);


                // order_additinal_Str for validation and sending to web service
                if (index == 0) // that's mean yes chosen
                {
                    order_additinal_Str = "1";

                    if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                        // checking if parts is null get parts from parts api
                        // and if parts if usable get parts from json in details api

                        if (additional.equals("1"))
                        {
                            showProducts_edit(parts_json);
                            parts_lay.setVisibility(View.VISIBLE);

                        }else if (additional.equals("0")){
                            get_Parts();
                            parts_lay.setVisibility(View.VISIBLE);
                            dialog_bar.show();
                        }



                    } else { // that's mean no parts needed

                        Snackbar snackbar = Snackbar.make(main_make_order_lay, getString(R.string.no_connect), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.connect_snackbar), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    }
                                });

                        snackbar.show();
                    }

                } else {

                    order_additinal_Str = "0";
                    parts_lay.setVisibility(View.GONE);
                    productsArrayList_Parts.clear(); //clear list
//                    productsAdapter_Parts.notifyDataSetChanged();
                }

                Log.d("asfdasf",order_additinal_Str);
            }
        });




        locations_edt = findViewById(R.id.location_order_edt);
        location_btn = findViewById(R.id.location_btn);
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize Google Play Services
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted


                        Intent intent = new Intent(Edit_Order_Activity.this, MapActivity.class);
                        intent.putExtra("saved_lat",mLatitude);
                        intent.putExtra("saved_lng",mLongitude);
                        startActivityForResult(intent, RESULT_GET_LOCATION);

                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                } else {

                    Intent intent = new Intent(Edit_Order_Activity.this, MapActivity.class);
                    startActivityForResult(intent, RESULT_GET_LOCATION);

                }
            }
        });


        order_image = findViewById(R.id.uplod_img1);
        order_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_code = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        order_image2 = findViewById(R.id.uplod_img2);
        order_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_code = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        edit_Order_btn = findViewById(R.id.post_Order);
        edit_Order_btn.setText(R.string.edit_order);
        edit_Order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        edit_Order();


                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(main_make_order_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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

        get_Details();

    }

    public static class DatePickerDialogTheme1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), AlertDialog.BUTTON_NEUTRAL, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            pref_date_edt.setText(year + "-" + (month + 1) + "-" + day);

            if (pref_date_edt.getText().length() > 0) {
                pref_date_edt.setError(null);
            }


        }
    }


    String additional;
    String parts_json;

    private void get_Details() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/getOrderDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                JSONObject desc = main_ob.getJSONObject("Data");

                                desc_edt.setText(desc.getString("desc"));


                                RadioButton urgent = findViewById(R.id.urgent);
                                RadioButton normal = findViewById(R.id.normal);

                                if (desc.getString("type").equals("u"))
                                {
                                    urgent.setChecked(true);
                                    normal.setChecked(false);

                                    order_Type_Str = "u";
                                }else {

                                    urgent.setChecked(false);
                                    normal.setChecked(true);

                                    order_Type_Str = "n";

                                }

                                pref_date_edt.setText(desc.getString("date"));

                                RadioButton additional_true = findViewById(R.id.additional_true);
                                RadioButton additional_false = findViewById(R.id.additional_false);


                                additional = desc.getString("additional_parts");

                                if (additional.equals("1")) // that's mean yes chosen
                                {
                                    additional_true.setChecked(true);
                                    additional_false.setChecked(false);

                                    order_additinal_Str = "1";


                                    parts_json = String.valueOf(desc);

                                    showProducts_edit(parts_json);

                                    parts_lay.setVisibility(View.VISIBLE);




                                } else {

                                    additional_true.setChecked(false);
                                    additional_false.setChecked(true);

                                    order_additinal_Str = "0";
                                    parts_lay.setVisibility(View.GONE);
                                    productsArrayList_Parts.clear(); //clear list
                                    //productsAdapter_Parts.notifyDataSetChanged();
                                }

                                mLatitude = desc.getString("lat");
                                mLongitude = desc.getString("lng");

                                address_edt.setText(desc.getString("address"));


                              //  this parm for getting all parts if parts null

                                sub_ser_id = desc.getString("sub_cat_id");


                                JSONArray images_array = desc.getJSONArray("images");

//
//                                for (int i = 0; i < images_array.length(); i++) {
//
//                                    String img1_url = images_array.getJSONObject(0).getString("url");
//                                    String img1_id = images_array.getJSONObject(0).getString("id");
//                                }


                                if (images_array !=null) {


                                    if(images_array.length() == 0) {

                                        Log.d("sdfds","dsfdsfgds");

                                    }else if (images_array.length() == 1) {



                                        String img1_url = images_array.getJSONObject(0).getString("url");
                                        String img1_id = images_array.getJSONObject(0).getString("id");


                                        Glide.with(getApplicationContext())
                                                .load(img1_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(order_image);

                                        order_image.setTag(img1_id);



                                    }else if (images_array.length() == 2){


                                        String img1_url = images_array.getJSONObject(0).getString("url");
                                        String img1_id = images_array.getJSONObject(0).getString("id");


                                        Glide.with(getApplicationContext())
                                                .load(img1_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(order_image);

                                        order_image.setTag(img1_id);


                                        String img2_url = images_array.getJSONObject(1).getString("url");
                                        String img2_id = images_array.getJSONObject(1).getString("id");


                                        Glide.with(getApplicationContext())
                                                .load(img2_url)
                                                .animate(R.anim.from_down)
                                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                .into(order_image2);

                                        order_image2.setTag(img2_id);

                                    }

                                }





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

                                Utility.dialog_error(Edit_Order_Activity.this, s.toString());

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
                params.put("order_id", order_id);


                Log.e("parms", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }




    private void get_Parts() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/getAvailableParts",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                showProducts_empty(response);
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

                                Utility.dialog_error(Edit_Order_Activity.this, s.toString());

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

                        Toast.makeText(getApplicationContext(), R.string.server_down , Toast.LENGTH_LONG).show();

                    }

                })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("sub_service_id", sub_ser_id);


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

    private void showProducts_edit(String json) {

        ParseGet_Parts_EDIT parseProducts;
        parseProducts = new ParseGet_Parts_EDIT(json);

        productsArrayList_Parts = parseProducts.parseProducts();

    //    if (!productsArrayList_Parts.isEmpty()) {

            productsAdapter_Parts = new Get_Parts_adapter(this, productsArrayList_Parts);

            recyclerView_get_parts.setAdapter(productsAdapter_Parts);

     //   } else {
            productsAdapter_Parts.notifyDataSetChanged();
     //   }

    }



    private void showProducts_empty(String json) {

        ParseGet_Parts parseProducts;
        parseProducts = new ParseGet_Parts(json);

        productsArrayList_Parts = parseProducts.parseProducts();

      //  if (!productsArrayList_Parts.isEmpty()) {

            productsAdapter_Parts = new Get_Parts_adapter(this, productsArrayList_Parts);

            recyclerView_get_parts.setAdapter(productsAdapter_Parts);

     //   } else {
            productsAdapter_Parts.notifyDataSetChanged();
    //    }

    }

    ArrayList<Get_Parts_Items> productsArrayList_Parts = new ArrayList<>();





    private static final int RESULT_GET_LOCATION = 22;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // map back data
        if (requestCode == RESULT_GET_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(MapActivity.KEY_LATITUDE)) {

                    mLatitude = data.getStringExtra(MapActivity.KEY_LATITUDE);
                    mLongitude = data.getStringExtra(MapActivity.KEY_LONGITUDE);
                    address_edt.setText(data.getStringExtra(MapActivity.KEY_Address));

                    //  Toast.makeText(Register_user_Activity.this, getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();

                    Log.d("safdsfsa", mLatitude);
                    Log.d("safdsfsa", mLongitude);
                    Log.d("safdsfsa", address_str);
//                    mMapActivityIv.setVisibility(View.GONE);
//                    Picasso.with(this)
//                            .load(StaticMap.getStaticMap(mLatitude, mLongitude))
//                            .placeholder(R.drawable.place_holder)
//                            .into(mLocationIv);
                }
            }
        }


        //  gallery get data

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView


                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float bitmapRatio = (float) width / (float) height;
                if (bitmapRatio > 0) {
                    width = 500;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = 500;
                    width = (int) (height * bitmapRatio);
                }
                Bitmap image_thumb = Bitmap.createScaledBitmap(bitmap, width, height, true);


                if (btn_code == 1) {

                    order_image.setImageBitmap(image_thumb);
                    image1 = getStringImage(bitmap);

                } else if (btn_code == 2) {
                    order_image2.setImageBitmap(image_thumb);

                    image2 = getStringImage(bitmap);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this).setTitle(R.string.loc_permission_need)
                        .setMessage(R.string.app_need_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Edit_Order_Activity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    Date date1;
    Date date2;
    int order_type;
 //   int order_parts;

    public boolean validate() {
        boolean valid = true;

        jsonArray_parts = new JSONArray();

        if (order_additinal_Str .equals("1"))
        {
            get_Parts_Json();
        }


        try {


            desc_str = desc_edt.getText().toString().trim();

            address_str = address_edt.getText().toString().trim();

            //radio button of order additinal 1 and 0 in change radio listner

            int radioButton_order_type = radioGroup_order_Type.getCheckedRadioButtonId();
            View radio_type_view = radioGroup_order_Type.findViewById(radioButton_order_type);
            order_type = radioGroup_order_Type.indexOfChild(radio_type_view);


            pref_date_str = pref_date_edt.getText().toString().trim();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String ss = sdf.format(cal.getTime());
            Log.d("sssss", ss);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date1 = formatter.parse(ss);
            date2 = formatter.parse(pref_date_str);
            Log.d("sssss", date2.toString());


        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (desc_str.isEmpty() || desc_str.length() <= 10) {
            desc_edt.setError("Description must be at least 10 char");
            scrollToPosition((View) desc_edt.getParent());
            valid = false;
        } else {
            desc_edt.setError(null);

        }
        Log.d("safsaf",desc_str);


        if (order_type == 0) {

            order_Type_Str = "u";

        } else {

            order_Type_Str = "n";

        }

        Log.d("type",order_Type_Str);

        if (pref_date_str.isEmpty() || date2.compareTo(date1) < 0) {
            pref_date_edt.setError("date must be after or equal today");
            scrollToPosition((View) pref_date_edt.getParent());
            Log.e("ComparingDates ", "SelectedIsBigger");
            valid = false;
        } else {
            pref_date_edt.setError(null);

            Log.e("ComparingDates ", "TodayIsBigger");

        }




        if (TextUtils.isEmpty(mLatitude) && TextUtils.isEmpty(mLongitude)) {
            mLatitude = sessionManager.getUserDetails().get("LATITUDE");
            mLongitude = sessionManager.getUserDetails().get("LONGITUDE");
            valid = false;
        }


        if (address_str.isEmpty()) {
            address_edt.setError(getString(R.string.valid_address));
            scrollToPosition((View) address_edt.getParent());
            valid = false;
        } else {
            address_edt.setError(null);

        }


        Log.d("parts", jsonArray_parts.toString());

        if (jsonArray_parts.toString().equals("[]") && order_additinal_Str .equals("1")) {
            scrollToPosition((View) parts_lay.getParent());
            Toast.makeText(getApplicationContext(), "Please Choose Parts", Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    private void scrollToPosition(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scroll_view_order.smoothScrollTo(0, view.getTop());
            }
        });
    }


    public void get_Parts_Json() {

        int childCount = recyclerView_get_parts.getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (recyclerView_get_parts.findViewHolderForLayoutPosition(i) instanceof Get_Parts_adapter.RecyclerViewHolder) {

                try {
                    JSONObject jsonObject = new JSONObject();

                    if (productsAdapter_Parts.productsArrayList.get(i).getcheckbox()) {
                        jsonObject.put("id", productsAdapter_Parts.productsArrayList.get(i).getProductId());
                        jsonObject.put("qty", productsAdapter_Parts.productsArrayList.get(i).getQuantity());

                        jsonArray_parts.put(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        Log.e("printAllEditTextValues", jsonArray_parts.toString());


    }


    ImageView order_image, order_image2;
    private int PICK_IMAGE_REQUEST = 33;

    String image1 = "", image2 = "";
    String final_img = "";

    Uri filePath;

    // encode to base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();

        //"data:image/jpeg;base64,"
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("tess", encodedImage);
        return encodedImage;
    }






    private void edit_Order() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/editOrder",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                dialog_bar.dismiss();
                                Toast.makeText(getApplicationContext(),"Order Edited Successfully",Toast.LENGTH_LONG).show();
                                Intent go_to_main = new Intent(getApplicationContext() , MainActivity.class);
                                go_to_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(go_to_main);


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

                                Utility.dialog_error(Edit_Order_Activity.this, s.toString());

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

                JSONArray deleted_imgs = new JSONArray();

                if (!image1.equals("") && !image2.equals("")) {

                    final_img = image1 + "_|||_" + image2;

                    Log.d("dfdsf", String.valueOf(order_image.getTag()));
                    deleted_imgs.put(order_image.getTag());
                    deleted_imgs.put(order_image2.getTag());

                    Log.d("sasafsfsa",deleted_imgs.toString());


                } else if (!image1.equals("") && image2.equals("")) {

                    final_img = image1;

                    deleted_imgs.put(order_image.getTag());

                } else if (image1.equals("") && !image2.equals("")) {

                    final_img = image2;
                    deleted_imgs.put(order_image2.getTag());
                }

                Log.d("dsfdsf",final_img);

                Log.d("sfsas",deleted_imgs.toString());



                Map<String, String> params = new HashMap<>();
                params.put("order_id", order_id);
                params.put("sub_cat_id", sub_ser_id);
                params.put("desc", desc_str);
                params.put("type", order_Type_Str);
                params.put("date", pref_date_str);
                params.put("lat", mLatitude);
                params.put("lng", mLongitude);
                params.put("address", address_str);
                params.put("additional_parts", order_additinal_Str);
                params.put("parts", jsonArray_parts.toString());
                params.put("images_for_clarification", final_img);
                params.put("imagesForDeleted", deleted_imgs.toString());

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
