package com.redray.khadoomhome.PROVIDER.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.AppController;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Update_Profile_prov_Activity extends AppCompatActivity {

    KProgressHUD dialog_bar;
    SessionManager session;

    ScrollView main_scroll_lay;


    private ArrayList<String> country_array;
    private ArrayList<String> id_country;

    private ArrayList<String> city_array;
    private ArrayList<String> id_city;

    private Spinner spinner_count;
    private Spinner spinner_city;
    // android.app.AlertDialog dialog;


    String country_id_pos;
    String city_id_pos;


    FrameLayout main_reg_lay;
    // data of birth , image , lat , lng
    EditText username_edt, phone_num_edt, email_edt, address_edt, neighborhood_edt, build_num_edt, apart_num_edt;
    EditText floor_edt, additnal_phone_edt, water_num_edt, landmark_edt , company_name_edt,licences_num_edt;





    //lat , lng , spinners
    String userName_str = "",
            phone_num_str = "",
            email_str = "",
            address_str = "",
            neighborhood_str = "",
            building_num_str = "";
    String apart_num_str = "",
            floor_str = "",
            additnal_phone_str = "",
            water_num_str = "",
            landmar_str = "",
            company_name_str="",
            licences_num_str="",
            owner_id_base_img="",
            license_base_img="";


    CircleImageView profile_image;

    ImageView owner_id_img , license_img;

    private int PICK_IMAGE_Profile = 55;
    private int PICK_IMAGE_REQUEST = 33;
    int imgePicker=0;

    Uri filePath;
    private Bitmap bitmap;




    String social_id = "";

    String imageURL = "";

    TimeZone tz;


    Button register_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_prov);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        session = new SessionManager(getApplicationContext());

        main_scroll_lay = findViewById(R.id.update_profile_scroll_view);


        session = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(Update_Profile_prov_Activity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        //Initializing the ArrayList
        country_array = new ArrayList<>();
        id_country = new ArrayList<>();

        city_array = new ArrayList<>();
        id_city = new ArrayList<>();

        spinner_count = findViewById(R.id.spinner_country);
        spinner_city = findViewById(R.id.spinner_city);

        main_reg_lay = findViewById(R.id.main_reg_lay);

        username_edt = findViewById(R.id.username_reg);
        phone_num_edt = findViewById(R.id.mobile_Num_reg);
        email_edt = findViewById(R.id.mail_reg);

        //data of birth
        //location
        //image
        //spinners


        address_edt = findViewById(R.id.address_reg);
        neighborhood_edt = findViewById(R.id.neighborhood_reg);
        build_num_edt = findViewById(R.id.build_num_reg);
        apart_num_edt = findViewById(R.id.apart_num_reg);
        floor_edt = findViewById(R.id.floor_reg);
        additnal_phone_edt = findViewById(R.id.additional_phone_reg);
        water_num_edt = findViewById(R.id.water_num_reg);
        landmark_edt = findViewById(R.id.landmark_reg);

        company_name_edt = findViewById(R.id.company_name_upd);
        licences_num_edt = findViewById(R.id.license_upd);




        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_Profile);
            }
        });



        license_img = findViewById(R.id.license_img);
        license_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgePicker=1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });



        owner_id_img = findViewById(R.id.owner_id_img);
        owner_id_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgePicker=2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });



        if (Check_Con.getInstance(this).isOnline()) {


            dialog_bar.show();

            getDetails_Profile();

            spinner_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("asfsaf", id_country.get(position));

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

                    Log.d("asfsaf", country_id_pos);

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


            Snackbar snackbar = Snackbar.make(main_reg_lay, getString(R.string.no_connect), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.connect_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }







        tz = TimeZone.getDefault();
        Log.d("timzone", tz.getID());

        register_user = findViewById(R.id.register_user);
        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {
//                registerSaveBtn.setEnabled(true);
//                return;

                        dialog_bar.show();

                        sendUpdateRequest();

                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(main_reg_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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

            // spinners , locations

            // required
            userName_str = username_edt.getText().toString().trim();
            phone_num_str = phone_num_edt.getText().toString().trim();
            email_str = email_edt.getText().toString().trim();
            address_str = address_edt.getText().toString().trim();
            neighborhood_str = neighborhood_edt.getText().toString().trim();
            building_num_str = build_num_edt.getText().toString().trim();


            //optinal
            apart_num_str = apart_num_edt.getText().toString().trim();
            floor_str = floor_edt.getText().toString().trim();
            additnal_phone_str = additnal_phone_edt.getText().toString().trim();
            water_num_str = water_num_edt.getText().toString().trim();
            landmar_str = landmark_edt.getText().toString().trim();

            company_name_str = company_name_edt.getText().toString().trim();
            licences_num_str = licences_num_edt.getText().toString().trim();


        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (userName_str.isEmpty() || userName_str.length() <= 6) {
            username_edt.setError(getString(R.string.valid_user));
            scrollToPosition((View) username_edt.getParent());
            valid = false;
        } else {
            username_edt.setError(null);

        }

        if (phone_num_str.isEmpty() || !Patterns.PHONE.matcher(phone_num_str).matches() || !Utility.isValidMobile(phone_num_str)) {
            phone_num_edt.setError(getString(R.string.valid_number));
            scrollToPosition((View) phone_num_edt.getParent());
            valid = false;
        } else {
            phone_num_edt.setError(null);

        }


        if (email_str.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_str).matches() || !Utility.validate(email_str)) {
            email_edt.setError(getString(R.string.valid_email));
            scrollToPosition((View) email_edt.getParent());
            valid = false;
        } else {
            email_edt.setError(null);

        }


        if (address_str.isEmpty()) {
            address_edt.setError(getString(R.string.valid_address));
            scrollToPosition((View) address_edt.getParent());
            valid = false;
        } else {
            address_edt.setError(null);

        }


        if (neighborhood_str.isEmpty()) {
            neighborhood_edt.setError(getString(R.string.valid_neighborhood));
            scrollToPosition((View) neighborhood_edt.getParent());
            valid = false;
        } else {
            neighborhood_edt.setError(null);

        }


        if (building_num_str.isEmpty()) {
            build_num_edt.setError(getString(R.string.valid_build));
            scrollToPosition((View) build_num_edt.getParent());
            valid = false;
        } else {
            build_num_edt.setError(null);

        }






        if (company_name_str.isEmpty()) {
            company_name_edt.setError(getString(R.string.valid_compan_name));
            scrollToPosition((View) company_name_edt.getParent());
            valid = false;
        } else {
            company_name_edt.setError(null);

        }

        if (licences_num_str.isEmpty()) {
            licences_num_edt.setError(getString(R.string.valid_license));
            scrollToPosition((View) licences_num_edt.getParent());
            valid = false;
        } else {
            licences_num_edt.setError(null);

        }


        return valid;
    }

    private void scrollToPosition(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                main_scroll_lay.smoothScrollTo(0, view.getTop());
            }
        });
    }


    public void getDetails_Profile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/spProfileData",
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
                             //   String id_user = stat_reg.getString("id");

                                userName_str = stat_reg.getString("f_name");
                                username_edt.setText(userName_str);

                                email_str = stat_reg.getString("email");
                                email_edt.setText(email_str);

                                phone_num_str = stat_reg.getString("phone_num");
                                phone_num_edt.setText(phone_num_str);

                                additnal_phone_str = stat_reg.getString("additional_num");
                                additnal_phone_edt.setText(additnal_phone_str);

                                address_str = stat_reg.getString("address");
                                address_edt.setText(address_str);


                                String profile_img_str = stat_reg.getString("profile_img");
                                if (profile_img_str.equals("")) {

                                    Glide.with(getApplicationContext()).load(R.drawable.profile_pic).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(profile_image);
                                } else {

                                    Glide.with(getApplicationContext()).load(profile_img_str).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(profile_image);
                                }


                                landmar_str = stat_reg.getString("landmark");
                                landmark_edt.setText(landmar_str);

                                water_num_str = stat_reg.getString("water_meter_num");
                                water_num_edt.setText(water_num_str);

                                floor_str = stat_reg.getString("floor_number");
                                floor_edt.setText(floor_str);

                                building_num_str = stat_reg.getString("building_number");
                                build_num_edt.setText(building_num_str);

                                apart_num_str = stat_reg.getString("apartment_num");
                                apart_num_edt.setText(apart_num_str);


                                neighborhood_str = stat_reg.getString("Neighborhood");
                                neighborhood_edt.setText(neighborhood_str);


                                company_name_str = stat_reg.getString("company_name");
                                company_name_edt.setText(company_name_str);

                                licences_num_str = stat_reg.getString("license_num");
                                licences_num_edt.setText(licences_num_str);


                                /////////////////////////////////////////////////////////////////////////////////////


                                //waiting
                                country_id_selected = stat_reg.getString("country_id");
                                Log.d("selceted", country_id_selected);

                                city_id_selected = stat_reg.getString("city_id");
                                Log.d("selceted", city_id_selected);

                                String license_img_str = stat_reg.getString("license_img");
                                if (license_img_str.equals("")) {

                                    Glide.with(getApplicationContext()).load(R.drawable.upload_icon).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(license_img);
                                } else {

                                    Glide.with(getApplicationContext()).load(license_img_str).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(license_img);
                                }



                                String owner_id_img_str = stat_reg.getString("owner_id_img");
                                if (owner_id_img_str.equals("")) {

                                    Glide.with(getApplicationContext()).load(R.drawable.upload_icon).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(owner_id_img);
                                } else {

                                    Glide.with(getApplicationContext()).load(owner_id_img_str).thumbnail(0.5f)
                                            .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(owner_id_img);
                                }


                                    get_country();




                                ///////////////////////////////////////////////////////////////////////////////////



                                //////////// not used
                              //  String timeZone = stat_reg.getString("timeZone");
                              //  String lang = stat_reg.getString("lang");



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

                                Utility.dialog_error(Update_Profile_prov_Activity.this, s.toString());

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


    String country_id_selected;
    String city_id_selected;


    public void sendUpdateRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/editSpProfile",
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


                                JSONObject stat_reg = obj.getJSONObject("Data");
                                String user_ID = stat_reg.getString("id");
                                String username = stat_reg.getString("f_name");
                                String email_user = stat_reg.getString("email");
                                String address_user = stat_reg.getString("address");
                                String lat_user = stat_reg.getString("lat");
                                String lng_user = stat_reg.getString("lng");
                                String profile_img_user = stat_reg.getString("profile_img");
                                String type_user = stat_reg.getString("type");


                                // save session here
                                session.createLoginSession(username, "0", profile_img_user, email_user,
                                        type_user, user_ID, address_user, lat_user, lng_user);


                                Intent go_to_main = new Intent(getApplicationContext(), MainActivity.class);
                                go_to_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(go_to_main);
                                finish();


                                Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();

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

                                Utility.dialog_error(Update_Profile_prov_Activity.this, s.toString());

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

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                String image;
                if (bitmap == null) {
                    image = imageURL;
                } else {
                    image = getStringImage(bitmap);
                }


                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                params.put("f_name", userName_str);
                params.put("email", email_str);
                params.put("lang", session.getLang());
                params.put("country_id", country_id_pos);
                params.put("city_id", city_id_pos);
                params.put("building_number", building_num_str);
                params.put("address", address_str);
                params.put("Neighborhood", neighborhood_str);
                params.put("phone_num", phone_num_str);

                params.put("timeZone", tz.getID());
                //android  0
                params.put("source", "0");
                //if social
                params.put("social_id", social_id);
                //firebase token
                params.put("token", refreshedToken);



                params.put("additional_num", additnal_phone_str);
                params.put("apartment_num", apart_num_str);
                params.put("floor_number", floor_str);
                params.put("profile_img", image);
                params.put("landmark", landmar_str);


                params.put("Water_meter_num", water_num_str);

                params.put("company_name", company_name_str);
                params.put("license_num", licences_num_str);



                params.put("owner_id_img", owner_id_base_img);
                params.put("license_img", license_base_img);



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


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();

        //"data:image/jpeg;base64,"
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("tess", encodedImage);
        return encodedImage;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //  profile image get data
        if (requestCode == PICK_IMAGE_Profile && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
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


                profile_image.setImageBitmap(image_thumb);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }





        Bitmap bitmap_data_imgs;


        //  image owner id and license image get data
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap_data_imgs = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView


                int width = bitmap_data_imgs.getWidth();
                int height = bitmap_data_imgs.getHeight();

                float bitmapRatio = (float) width / (float) height;
                if (bitmapRatio > 0) {
                    width = 500;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = 500;
                    width = (int) (height * bitmapRatio);
                }
                Bitmap image_thumb = Bitmap.createScaledBitmap(bitmap_data_imgs, width, height, true);



                //important note we clear bitmap in 2 and 3
                // for checking bitmap if null with profile in social login

                // 1 for license
                //2 for owner id

                if (imgePicker == 1)
                {
                    license_img.setBackground(null);
                    license_img.setImageBitmap(image_thumb);
                    license_base_img = getStringImage(image_thumb);

                    //  bitmap = null;
                    //add_img_txt1.setVisibility(View.GONE);

                }else if (imgePicker ==2)
                {
                    owner_id_img.setBackground(null);
                    owner_id_img.setImageBitmap(image_thumb);
                    owner_id_base_img = getStringImage(image_thumb);

                    //  bitmap = null;
                    //   add_img_txt2.setVisibility(View.GONE);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                          Toast.makeText(getApplicationContext(), R.string.server_down,Toast.LENGTH_LONG).show();
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


        if (firstTimeRunning_country) {
            // start register activity.

            setSpinnerItemById_country(spinner_count, country_id_selected);
            firstTimeRunning_country = false;
        } else {
            // start login activity.
            Log.d("sfdsafsf","not first");
        }

    }



    private void get_city(final String Country_id) {
        //Creating a string request

        String tag_json_obj = "json_obj_city";

        //  dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "locations/cities",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        city_array.clear();
                        id_city.clear();

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            JSONArray jsonarray = main_ob.getJSONArray("Data");

                            Log.d("tessst", response);
                            //Parsing the fetched Json String to JSON Object
                            //    JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                //   String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");
                                id_city.add(id);
                                Log.d("safsafas", id_city.toString());

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
                params.put("lang", session.getLang());

                Log.e("parm", params.toString());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void parse_getCities(JSONArray j) {
        //Traversing through all the items in the json array

        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                city_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //  dialog.dismiss();


//        if(city_array.isEmpty())
//        {
//            city_array = new ArrayList<>();
//            city_array.add("No Areas");
//        }
        //Setting adapter to show the items in the spinner
        spinner_city.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, city_array));

        if (firstTimeRunning_city) {
            // start register activity.
            setSpinnerItemById_city(spinner_city, city_id_selected);
            firstTimeRunning_city = false;
        } else {
            // start login activity.
            Log.d("sfdsafsf","not first");
        }


    }


    boolean firstTimeRunning_country = true;
    boolean firstTimeRunning_city = true;

    public void setSpinnerItemById_country(Spinner spinner, String _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {

            String id = id_country.get(i);

            if (id.equals(_id)) {
                spinner.setSelection(i);
                Log.d("selected", id_country.get(i));
            }
        }
    }


    public void setSpinnerItemById_city(Spinner spinner, String _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {

            String id = id_city.get(i);

            if (id.equals(_id)) {
                spinner.setSelection(i);
                Log.d("selected", id_city.get(i));
            }
        }
    }




//    public void setSpinnerItemById_country(Spinner spinner, String _id) {
//        int spinnerCount = spinner.getCount();
//        for (int i = 0; i < spinnerCount; i++) {
//
//            String id;
//
//            if (spinner.getId() == R.id.spinner_country){
//
//                id = id_country.get(i);
//
//            }else {
//
//                id = id_city.get(i);
//            }
//
//            if (id.equals(_id)) {
//                spinner.setSelection(i);
//            }
//        }
//    }


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
