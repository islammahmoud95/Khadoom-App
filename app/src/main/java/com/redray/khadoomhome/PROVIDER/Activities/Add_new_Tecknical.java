package com.redray.khadoomhome.PROVIDER.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Add_new_Tecknical extends AppCompatActivity {

    private ArrayList<String> specialization_array;
    private ArrayList<String> id_speclization;
    private Spinner spinner_count;
    String spec_id_pos;
    KProgressHUD dialog_bar;
    SessionManager session;
    FrameLayout main_reg_lay;
    // data of birth , image
    EditText username_edt, phone_num_edt, pass_edt, pass_aga_edt, email_edt, address_edt;
    Button register_user;
    // spinners
    String userName_str = "",
            phone_num_str = "",
            pass_str = "",
            pass_aga_str = "",
            email_str = "";


    CircleImageView profile_image;


    private int PICK_IMAGE_Profile = 66;

    ScrollView reg_scroll_view;


    Uri filePath;
    private Bitmap bitmap;

    TimeZone tz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tecknical);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        session = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(Add_new_Tecknical.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        //Initializing the ArrayList
        specialization_array = new ArrayList<>();
        id_speclization = new ArrayList<>();


        spinner_count = findViewById(R.id.spinner_country);


        reg_scroll_view = findViewById(R.id.reg_tech_scroll_view);

        main_reg_lay = findViewById(R.id.main_reg_lay);

        username_edt = findViewById(R.id.username_reg);
        phone_num_edt = findViewById(R.id.mobile_Num_reg);
        pass_edt = findViewById(R.id.password_reg);
        pass_aga_edt = findViewById(R.id.confirm_pass_reg);
        email_edt = findViewById(R.id.mail_reg);


        address_edt = findViewById(R.id.address_reg);


        if (Check_Con.getInstance(this).isOnline()) {

            get_Sections_Technicals();
            dialog_bar.show();


            spinner_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.d("asfsaf", id_speclization.get(position));

                    spec_id_pos = id_speclization.get(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        } else {


            specialization_array.add(getString(R.string.no_connect));


            spinner_count.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, specialization_array));


            Snackbar snackbar = Snackbar.make(main_reg_lay, getString(R.string.no_connect), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.connect_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }


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


        tz = TimeZone.getDefault();
        Log.d("timzone", tz.getID());

        register_user = findViewById(R.id.register_tech);
        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {


                        dialog_bar.show();

                        sendRegisterRequest();

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

            // spinners

            // required
            userName_str = username_edt.getText().toString().trim();
            phone_num_str = phone_num_edt.getText().toString().trim();
            email_str = email_edt.getText().toString().trim();
            pass_str = pass_edt.getText().toString().trim();
            pass_aga_str = pass_aga_edt.getText().toString().trim();


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


        if (pass_str.isEmpty() || !Utility.passLength(pass_str)) {
            pass_edt.setError(getString(R.string.valid_pass));
            scrollToPosition((View) pass_edt.getParent());
            valid = false;
        } else {
            pass_edt.setError(null);

        }


        if (!Utility.isMatching(pass_str, pass_aga_str)) {
            pass_aga_edt.setError(getString(R.string.valid_confirm));
            scrollToPosition((View) pass_aga_edt.getParent());
            valid = false;
        } else {
            pass_aga_edt.setError(null);

        }


        return valid;
    }

    private void scrollToPosition(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                reg_scroll_view.smoothScrollTo(0, view.getTop());
            }
        });
    }


    public void sendRegisterRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/SpAddNewTechAPI",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {
                               // JSONObject data = obj.getJSONObject("Data");

                                Toast.makeText(getApplicationContext(), R.string.suc_reg, Toast.LENGTH_LONG).show();

                                dialog_bar.dismiss();
                                Intent go_to_tech = new Intent(getApplicationContext(),Get_Technicals.class);
                                go_to_tech.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(go_to_tech);
                                finish();

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

                                Utility.dialog_error(Add_new_Tecknical.this, s.toString());

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


                String image;
                if (bitmap == null) {
                         image = "";
                } else {
                    image = getStringImage(bitmap);
                }


                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                params.put("name", userName_str);
                params.put("email", email_str);
                params.put("phone_num", phone_num_str);
                params.put("password", pass_str);
                params.put("lang", session.getLang());
                params.put("timeZone", tz.getID());
                params.put("specialization_id", spec_id_pos);
                params.put("profile_img", image);



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
                session.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", session.getCookie());

                return headers;
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


    }


    private void get_Sections_Technicals() {
        //Creating a string request

        String tag_json_obj = "json_obj_count";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/getSectionsTechnicians",
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

                                id_speclization.add(id);
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
                specialization_array.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Setting adapter to show the items in the spinner

//        if (country_array.isEmpty())
//        {
//            country_array.add("No Countries");
//        }
        spinner_count.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specialization_array));

        dialog_bar.dismiss();
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
