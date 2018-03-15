package com.redray.khadoomhome.PROVIDER.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.all_users.Activites.Login_Activity;
import com.redray.khadoomhome.utilities.AppController;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Register_prov_Activity extends AppCompatActivity {


    private ArrayList<String> country_array;
    private ArrayList<String> id_country;

    private ArrayList<String> city_array;
    private ArrayList<String> id_city;

    private Spinner spinner_count;
    private Spinner spinner_city;


    String country_id_pos;
    String city_id_pos;

    KProgressHUD dialog_bar;

    SessionManager session;

    FrameLayout main_reg_lay;
    // data of birth , image
    EditText username_edt, phone_num_edt, pass_edt, pass_aga_edt, email_edt, address_edt, neighborhood_edt, build_num_edt,
            apart_num_edt;
    EditText floor_edt, additnal_phone_edt, water_num_edt, landmark_edt, company_name_edt, licences_num_edt;


    // spinners
    String userName_str = "",
            phone_num_str = "",
            pass_str = "",
            pass_aga_str = "",
            email_str = "",
            company_name_str = "",
            licences_num_str = "",
            address_str = "",
            neighborhood_str = "",
            building_num_str = "";
    String apart_num_str = "",
            floor_str = "",
            additnal_phone_str = "",
            water_num_str = "",
            landmar_str = "",
            owner_id_base_img = "",
            license_base_img = "";


    CircleImageView profile_image;

    ImageView owner_id_img, license_img;

    private int PICK_IMAGE_Profile = 33;
    private int PICK_IMAGE_REQUEST = 44;


    Uri filePath;
    private Bitmap bitmap;


    //social corner

    //google +
    private static final int RC_SIGN_IN = 11;
    private GoogleApiClient mGoogleApiClient;


    String social_id = "";
    CallbackManager callbackManager;
    public String social_email, userID, social_user_name = "";
    private static final String TWITTER_KEY = "Am6fWkh7oovmtYHxVGUBGfenY";
    private static final String TWITTER_SECRET = "heTUXdCtj6it8VYsJBhm2fWw5u3ErkYArxaoyoBEVrybZvvRRq";
    private TwitterAuthClient mTwitterAuthClient;
    ImageButton face, twitter;
    String imageURL = "";

    TimeZone tz;

    ScrollView reg_scroll_view;
    Button register_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_provider_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        session = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(Register_prov_Activity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        //Initializing the ArrayList
        country_array = new ArrayList<>();
        id_country = new ArrayList<>();

        city_array = new ArrayList<>();
        id_city = new ArrayList<>();

        spinner_count = findViewById(R.id.spinner_country);
        spinner_city = findViewById(R.id.spinner_city);

        reg_scroll_view = findViewById(R.id.reg_scroll_view);

        main_reg_lay = findViewById(R.id.main_reg_lay);

        username_edt = findViewById(R.id.username_reg);
        phone_num_edt = findViewById(R.id.mobile_Num_reg);
        pass_edt = findViewById(R.id.password_reg);
        pass_aga_edt = findViewById(R.id.confirm_pass_reg);
        email_edt = findViewById(R.id.mail_reg);
        company_name_edt = findViewById(R.id.company_name_reg);
        licences_num_edt = findViewById(R.id.license_reg);

        //data of birth
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


        callbackManager = CallbackManager.Factory.create();
        face = findViewById(R.id.facebook_reg);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_bar.show();
                LoginManager.getInstance().logInWithReadPermissions(Register_prov_Activity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());

                        setProfileToView(object);

                        username_edt.setText(social_user_name);


                        email_edt.setText(social_email);
                        email_edt.setEnabled(false);

                        //   imageURL ="https://graph.facebook.com/" + userID + "/picture?type=large";


                        Bundle params = new Bundle();
                        params.putString("fields", "id,email,picture.type(large)");
                        new GraphRequest(AccessToken.getCurrentAccessToken(), userID, params, HttpMethod.GET, new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {
                                        JSONObject data = response.getJSONObject();
                                        if (data.has("picture")) {

                                            imageURL = data.getJSONObject("picture").getJSONObject("data").getString("url");


                                            Glide.with(getApplicationContext()).load(imageURL).thumbnail(0.5f)
                                                    .animate(R.anim.from_down)
                                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                    .into(profile_image);


                                            dialog_bar.dismiss();
                                            Log.v("MYIMAGE", String.valueOf(imageURL));

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).executeAsync();


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                dialog_bar.dismiss();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Register_prov_Activity.this, R.string.falid_facebook, Toast.LENGTH_SHORT).show();
                dialog_bar.dismiss();
                Log.e("FACEBOOKERROR", String.valueOf(exception));
            }
        });


        twitter = findViewById(R.id.twitter_reg);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_bar.show();

                mTwitterAuthClient = new TwitterAuthClient();

                mTwitterAuthClient.authorize(Register_prov_Activity.this, new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {

                        TwitterSession session = twitterSessionResult.data;

                        TwitterAuthClient authClient = new TwitterAuthClient();
                        authClient.requestEmail(session, new Callback<String>() {
                            @Override
                            public void success(Result<String> result) {
                                social_email = result.data;

                                email_edt.setEnabled(false);
                                email_edt.setText(social_email);

                                Log.d("EMAIL", social_email);

                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.e("TwitterException", exception.getMessage() + "");
                                dialog_bar.dismiss();
                            }
                        });
                        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
                        userResult.enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {

                                User user = userResult.data;
                                try {
                                    social_id = String.valueOf(user.getId());
                                    Log.d("userid", social_id);

                                    imageURL = user.profileImageUrl;


                                    social_user_name = user.name;
                                    username_edt.setText(social_user_name);

                                    Glide.with(getApplicationContext()).load(imageURL).thumbnail(0.5f).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(profile_image);

                                    dialog_bar.dismiss();

                                    mTwitterAuthClient = null;

                                } catch (Exception e) {
                                    Log.e("Exception", e.getMessage() + "");

                                }
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.e("TwitterException", e.getMessage() + "");
                                dialog_bar.dismiss();
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e("TwitterException", e.getMessage() + "");
                        mTwitterAuthClient = null;
                        dialog_bar.dismiss();
                    }
                });
            }
        });

        ImageButton google_sign_btn = findViewById(R.id.google_reg);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_bar.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        if (Check_Con.getInstance(this).isOnline()) {

            get_country();
            dialog_bar.show();


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


        license_img = findViewById(R.id.uplod_license_img);
        license_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgePicker = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        owner_id_img = findViewById(R.id.uplod_ID_img);
        owner_id_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgePicker = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), PICK_IMAGE_REQUEST);
            }
        });


        tz = TimeZone.getDefault();
        Log.d("timzone", tz.getID());

        register_user = findViewById(R.id.register_user);
        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        show_terms();

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

    int imgePicker = 0;


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
            address_str = address_edt.getText().toString().trim();
            neighborhood_str = neighborhood_edt.getText().toString().trim();
            building_num_str = build_num_edt.getText().toString().trim();

            company_name_str = company_name_edt.getText().toString().trim();
            licences_num_str = licences_num_edt.getText().toString().trim();

            //optinal

            apart_num_str = apart_num_edt.getText().toString().trim();
            floor_str = floor_edt.getText().toString().trim();
            additnal_phone_str = additnal_phone_edt.getText().toString().trim();
            water_num_str = water_num_edt.getText().toString().trim();
            landmar_str = landmark_edt.getText().toString().trim();


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


        if (TextUtils.isEmpty(company_name_str) && TextUtils.isEmpty(company_name_str)) {
            company_name_edt.setError(getString(R.string.valid_compan_name));
            scrollToPosition((View) company_name_edt.getParent());
            valid = false;
        } else {
            company_name_edt.setError(null);

        }

        if (TextUtils.isEmpty(licences_num_str) && TextUtils.isEmpty(licences_num_str)) {
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
                reg_scroll_view.smoothScrollTo(0, view.getTop());
            }
        });
    }


    public void sendRegisterRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sp/register",
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
                                String reg_ID = data.getString("reg_id");
                                dialog_bar.dismiss();


                                Toast.makeText(getApplicationContext(), R.string.suc_reg, Toast.LENGTH_LONG).show();
                                add_services(reg_ID);


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

                                Utility.dialog_error(Register_prov_Activity.this, s.toString());

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


                params.put("company_name", company_name_str);
                params.put("license_num", licences_num_str);
                params.put("owner_id_img", owner_id_base_img);
                params.put("license_img", license_base_img);


                params.put("password", pass_str);
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

                //for logining by facebook
                params.put("water_meter_num", water_num_str);


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

    Dialog dialog;

    private void add_services(final String reg_ID) {
        dialog = new Dialog(Register_prov_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_add_services);
        }

        Button add_now = dialog.findViewById(R.id.now_btn);
        add_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_SERVICES = new Intent(getApplicationContext(), Add_Service.class);
                go_to_SERVICES.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                go_to_SERVICES.putExtra("REG_ID", reg_ID);
                startActivity(go_to_SERVICES);
                dialog.dismiss();
                finish();

            }
        });


        Button add_later = dialog.findViewById(R.id.later_btn);
        //  mRegister.setTypeface(mBaseTextFont);
        add_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_login = new Intent(getApplicationContext(), Login_Activity.class);
                go_to_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(go_to_login);
                dialog.dismiss();
                finish();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

        //google back data

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {


                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                if (account != null) {

                    social_id = account.getId();
                    Log.d("userid", social_id);

                    social_user_name = account.getDisplayName();
                    username_edt.setText(social_user_name);


                    social_email = account.getEmail();
                    email_edt.setText(social_email);

                    email_edt.setEnabled(false);


                    imageURL = String.valueOf(account.getPhotoUrl());

                } else {
                    Log.d("goooogle", "empty");
                }


                Glide.with(getApplicationContext()).load(imageURL).thumbnail(0.5f).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(profile_image);


                dialog_bar.dismiss();
                //  firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                Log.d("failled", "faaileed");
                dialog_bar.dismiss();

            }
        }


        //facebook social
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //twitter social
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);

        }


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

                if (imgePicker == 1) {
                    license_img.setBackground(null);
                    license_img.setImageBitmap(image_thumb);
                    license_base_img = getStringImage(image_thumb);

                    //  bitmap = null;
                    //add_img_txt1.setVisibility(View.GONE);

                } else if (imgePicker == 2) {
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


    public void show_terms() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "sitePolicy",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {
                                dialog_bar.dismiss();


                                JSONObject stat_reg = obj.getJSONObject("Data");
                                String text_tems = stat_reg.getString("text");
                                showTermsDialog(text_tems);

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

                                Utility.dialog_error(Register_prov_Activity.this, s.toString());

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

                params.put("lang", session.getLang());


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

    private void showTermsDialog(String terms) {
        dialog = new Dialog(Register_prov_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_terms);
        }
        TextView mTermsString = dialog.findViewById(R.id.reg_terms_tv);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mTermsString.setText(Html.fromHtml(terms, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            mTermsString.setText(Html.fromHtml(terms));
        }


        final AppCompatCheckBox mAgree = dialog.findViewById(R.id.reg_iAgree_checkbox);

        Button mRegister = dialog.findViewById(R.id.checked_reg_btn);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAgree.isChecked()) {

                    Toast.makeText(getApplicationContext(), R.string.accept_policy_reg, Toast.LENGTH_LONG).show();

                } else {

                    dialog.dismiss();
                    dialog_bar.show();
                    sendRegisterRequest();

                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                        Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
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
    }


    private void setProfileToView(JSONObject jsonObject) {
        try {

            social_email = jsonObject.getString("email");

            social_user_name = jsonObject.getString("name");

            userID = jsonObject.getString("id");

            social_id = userID;
            Log.d("userid", social_id);

            Log.v("PICTURE", userID);

            Log.v("EMAIL", social_email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
