package com.redray.khadoomhome.tickets.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.tickets.adapter.Get_Details_Tickets_adapter;
import com.redray.khadoomhome.tickets.models.Get_Details_Tickets_Items;
import com.redray.khadoomhome.tickets.parser.ParseGet_Details_tickets;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Ticket_details extends AppCompatActivity {


    String ticket_ID;


    KProgressHUD dialog_bar;
    SessionManager sessionManager;


    RecyclerView recyclerView_get_parts;
    LinearLayoutManager manager;
    Get_Details_Tickets_adapter productsAdapter_tickets;

    RelativeLayout ticket_details_lay;

    TextView ticket_closed_txt;

    EditText text_edt;
    ImageView send_btn;
    String text_str;


    private int PICK_IMAGE_REQUEST = 44;

    Uri filePath;
    ImageView uplod_img;
    String uplod_img_base = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent_id = getIntent();
        if (intent_id != null) {
            ticket_ID = intent_id.getStringExtra("Ticket_ID");
            Log.d("sfsfsaf", ticket_ID);
        }

        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());


        ticket_details_lay = findViewById(R.id.ticket_details_lay);

        ticket_closed_txt = findViewById(R.id.ticket_closed_txt);

        text_edt = findViewById(R.id.text_edt);
        send_btn = findViewById(R.id.send_btn);

        recyclerView_get_parts = findViewById(R.id.replies_recycle);
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


        uplod_img = findViewById(R.id.uplod_img);
        uplod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                    text_str = text_edt.getText().toString().trim();
                    if (!text_str.isEmpty()) {
                        send_reply();
                        dialog_bar.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Write Reply", Toast.LENGTH_LONG).show();
                    }


                } else {

                    Snackbar snackbar = Snackbar
                            .make(ticket_details_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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
    public void onResume() {
        super.onResume();
        Log.e("onresume", "1");


        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            dialog_bar.show();
            get_Tickets_details();


        } else {

            Snackbar snackbar = Snackbar
                    .make(ticket_details_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();

        }


    }




    private void get_Tickets_details() {
        //Creating a string request

        String tag_json_obj = "json_obj_city";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "getOneTicket",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject main_ob = new JSONObject(response);

                            Boolean status = main_ob.getBoolean("Status");
                            if (status) {

                                JSONObject data = main_ob.getJSONObject("Data");
                                String ticket_stat = data.getString("status");
                                // 0 mean ticket is open and 1 mean ticket is closed
                                if (ticket_stat.equals("1")) {
                                    text_edt.setVisibility(View.GONE);
                                    send_btn.setVisibility(View.GONE);
                                    uplod_img.setVisibility(View.GONE);
                                    ticket_closed_txt.setVisibility(View.VISIBLE);
                                }


                                showProducts(data.toString());
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

                                Utility.dialog_error(Ticket_details.this, s.toString());

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

                params.put("ticket_id", ticket_ID);
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
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
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

    private void showProducts(String json) {

        ParseGet_Details_tickets parseProducts;
        parseProducts = new ParseGet_Details_tickets(json);

        productsArrayList_Parts = parseProducts.parseProducts();

        //   if (!productsArrayList_Parts.isEmpty()) {

        productsAdapter_tickets = new Get_Details_Tickets_adapter(this, productsArrayList_Parts);

        recyclerView_get_parts.setAdapter(productsAdapter_tickets);

        //  } else {
        productsAdapter_tickets.notifyDataSetChanged();
        //  }

    }

    ArrayList<Get_Details_Tickets_Items> productsArrayList_Parts = new ArrayList<>();





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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

                uplod_img_base = getStringImage(image_thumb);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();

        //"data:image/jpeg;base64,"
        String encodedImage =  Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("tess",encodedImage);
        return encodedImage;
    }


    public void send_reply() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "postReplay",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                //reset values
                                text_edt.setText("");
                                uplod_img_base = "";

                                get_Tickets_details();

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

                                Utility.dialog_error(Ticket_details.this, s.toString());

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

                params.put("ticket_id", ticket_ID);
                params.put("content", text_str);
                params.put("attach", uplod_img_base);
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
