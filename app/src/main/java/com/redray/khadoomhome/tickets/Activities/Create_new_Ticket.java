package com.redray.khadoomhome.tickets.Activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Create_new_Ticket extends AppCompatActivity {


    KProgressHUD dialog_bar;
    SessionManager sessionManager;

    Button create_ticket_btn;

    LinearLayout create_ticket_lay;
    ScrollView scroll_ticket_lay;

    EditText reson_ticket_edt, describ_ticket_edt;
    String reson_ticket_str, describ_ticket_str;

    private int PICK_IMAGE_REQUEST = 44;
    Uri filePath;
    ImageView uplod_img;
    String uplod_img_base = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ticket);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        scroll_ticket_lay = findViewById(R.id.scroll_ticket_lay);
        create_ticket_lay = findViewById(R.id.create_ticket_lay);


        reson_ticket_edt = findViewById(R.id.reson_ticket_edt);
        describ_ticket_edt = findViewById(R.id.describ_ticket_edt);


        uplod_img = findViewById(R.id.upload_ticket_image);
        uplod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        create_ticket_btn = findViewById(R.id.create_ticket_btn);
        create_ticket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                    if (validate()) {

                        dialog_bar.show();

                        send_ticket();

                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(create_ticket_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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

                uplod_img.setBackground(null);
                uplod_img.setImageBitmap(image_thumb);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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


    private void scrollToPosition(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scroll_ticket_lay.smoothScrollTo(0, view.getTop());
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        try {

            // required
            reson_ticket_str = reson_ticket_edt.getText().toString().trim();
            describ_ticket_str = describ_ticket_edt.getText().toString().trim();


        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (reson_ticket_str.isEmpty() || reson_ticket_str.length() <= 6) {
            reson_ticket_edt.setError(getString(R.string.valid_user));
            scrollToPosition((View) reson_ticket_edt.getParent());
            valid = false;
        } else {
            reson_ticket_edt.setError(null);

        }


        if (describ_ticket_str.isEmpty() || describ_ticket_str.length() <= 6) {
            describ_ticket_edt.setError(getString(R.string.valid_user));
            scrollToPosition((View) describ_ticket_edt.getParent());
            valid = false;
        } else {
            describ_ticket_edt.setError(null);

        }


        return valid;
    }


    public void send_ticket() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "createNewTicket",
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

                                finish();

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

                                Utility.dialog_error(Create_new_Ticket.this, s.toString());

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

                params.put("reason", reson_ticket_str);
                params.put("content", describ_ticket_str);
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
