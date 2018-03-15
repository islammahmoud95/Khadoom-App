package com.redray.khadoomhome.all_users.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.PROVIDER.Activities.Register_prov_Activity;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.USER.Activities.Activition_Mobile_Activity;
import com.redray.khadoomhome.USER.Activities.Register_user_Activity;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login_Activity extends AppCompatActivity {

    TextView create_account_txt, forgot_pass_txt;
    EditText email_Tv_edt, pass_Tv_edt;
    String email_txt, passwod_txt;

    SessionManager sessionManager;

    LinearLayout main_layout_login;

    LinearLayout social_layout;

    KProgressHUD dialog_bar;


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
    ImageView profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dialog_bar = KProgressHUD.create(Login_Activity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        main_layout_login = findViewById(R.id.main_layout_login);
        social_layout = findViewById(R.id.social_layout);
        create_account_txt = findViewById(R.id.create_account_txt);
        forgot_pass_txt = findViewById(R.id.forgot_pass_txt);
        email_Tv_edt = findViewById(R.id.login_email_Tv);
        pass_Tv_edt = findViewById(R.id.login_pass_Tv);
        profile_img = (CircleImageView) findViewById(R.id.profile_img);

        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.getUser_Type().equals("3")) {
            create_account_txt.setVisibility(View.INVISIBLE);
            social_layout.setVisibility(View.GONE);
        }


        create_account_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionManager.getUser_Type().equals("1")) {
                    Intent register_Activity = new Intent(getApplicationContext(), Register_user_Activity.class);
                    startActivity(register_Activity);

                } else if (sessionManager.getUser_Type().equals("2")) {
                    Intent register_Activity = new Intent(getApplicationContext(), Register_prov_Activity.class);
                    startActivity(register_Activity);
                }

            }
        });


        forgot_pass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_pass = new Intent(getApplicationContext(), Forgot_Password.class);
                startActivity(forgot_pass);
            }
        });


        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        login_all();

                    }

                } else {

                    Snackbar snackbar = Snackbar
                            .make(main_layout_login, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


        callbackManager = CallbackManager.Factory.create();
        face = findViewById(R.id.login_face_btn);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_bar.show();
                LoginManager.getInstance().logInWithReadPermissions(Login_Activity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());

                        // here we call api web service to send user id
                        setProfileToView(object);


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
                                                    .animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                    .into(profile_img);


                                            dialog_bar.setDimAmount(0);
                                            Log.v("MYIMAGE", String.valueOf(imageURL));

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog_bar.dismiss();
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
                Toast.makeText(Login_Activity.this, R.string.error_login_facebook, Toast.LENGTH_SHORT).show();
                dialog_bar.dismiss();
                Log.e("FACEBOOKERROR", String.valueOf(exception));
            }
        });


        twitter = findViewById(R.id.login_twitter_btn);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_bar.show();

                mTwitterAuthClient = new TwitterAuthClient();

                mTwitterAuthClient.authorize(Login_Activity.this, new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {

                        TwitterSession session = twitterSessionResult.data;

                        TwitterAuthClient authClient = new TwitterAuthClient();
                        authClient.requestEmail(session, new Callback<String>() {
                            @Override
                            public void success(Result<String> result) {

                                social_email = result.data;
                                //   email_Tv_edt.setText(social_email);

                                Log.d("EMAIL", social_email);

                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.e("TwitterException", exception.getMessage() + "");
                                dialog_bar.dismiss();
                            }
                        });
                        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
                        userResult.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                            @Override
                            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {

                                com.twitter.sdk.android.core.models.User user = userResult.data;
                                try {
                                    social_id = String.valueOf(user.getId());
                                    Log.d("userid", social_id);

                                    imageURL = user.profileImageUrl;


                                    Glide.with(getApplicationContext()).load(imageURL).thumbnail(0.5f).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(profile_img);

                                    login_Auth_Social();


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

        ImageButton google_sign_btn = findViewById(R.id.login_google_btn);

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

    }


    public boolean validate() {
        boolean valid = true;

        try {

            email_txt = email_Tv_edt.getText().toString().trim();
            passwod_txt = pass_Tv_edt.getText().toString().trim();


        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (email_txt.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_txt).matches() || !Utility.validate(email_txt)) {
            email_Tv_edt.setError(getString(R.string.valid_email));
            valid = false;
        } else {
            email_Tv_edt.setError(null);

        }


        if (passwod_txt.isEmpty()) {
            pass_Tv_edt.setError(getString(R.string.valid_pass));
            valid = false;
        } else {
            pass_Tv_edt.setError(null);

        }


        return valid;
    }


    public void login_all() {

        String sub_URL = null;
        if (sessionManager.getUser_Type().equals("1")) {

            sub_URL = "users/login";

        } else if (sessionManager.getUser_Type().equals("2")) {

            sub_URL = "sp/login";

        }
        if (sessionManager.getUser_Type().equals("3")) {

            sub_URL = "tech/login";
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + sub_URL,
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
                                String stat_type = stat_reg.getString("activation_str");

                                // this for user
                                switch (stat_type) {
                                    case "waiting_Active": {

                                        String reg_id = stat_reg.getString("reg_id");
                                        Intent activ_reg = new Intent(getApplicationContext(), Activition_Mobile_Activity.class);
                                        activ_reg.putExtra("REG_ID", reg_id);
                                        startActivity(activ_reg);


                                        //this for provider
                                        break;
                                    }
                                    case "waiting_accept": {


                                        StringBuilder s = new StringBuilder(150);
                                        String street;
                                        JSONArray st = obj.getJSONArray("Errors");
                                        for (int i = 0; i < st.length(); i++) {
                                            street = st.getString(i);
                                            s.append(street);
                                            s.append("\n");
                                            Log.i("teeest", s.toString());
                                            // loop and add it to array or arraylist
                                        }


                                        Utility.dialog_error(Login_Activity.this, s.toString());


                                        //this for all
                                        break;
                                    }
                                    case "active": {

                                        Intent activ_reg = new Intent(getApplicationContext(), MainActivity.class);
                                        activ_reg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(activ_reg);

                                        String userName = stat_reg.getString("name");
                                        String userPic = stat_reg.getString("img");
                                        String userEmail = stat_reg.getString("email");
                                        String userType = stat_reg.getString("type");
                                        String userID = stat_reg.getString("id");
                                        String userAddress = stat_reg.getString("address");

                                        String lat = stat_reg.getString("lat");
                                        String lng = stat_reg.getString("lng");

                                        sessionManager.createLoginSession(userName, "0", userPic, userEmail, userType, userID, userAddress, lat, lng);

                                        Toast.makeText(getApplicationContext(), R.string.succ_login, Toast.LENGTH_LONG).show();

                                        //this for all
                                        break;
                                    }
                                    case "deactivated": {


                                        StringBuilder s = new StringBuilder(150);
                                        String street;
                                        JSONArray st = obj.getJSONArray("Errors");
                                        for (int i = 0; i < st.length(); i++) {
                                            street = st.getString(i);
                                            s.append(street);
                                            s.append("\n");
                                            Log.i("teeest", s.toString());
                                            // loop and add it to array or arraylist
                                        }


                                        Utility.dialog_error(Login_Activity.this, s.toString());

                                        break;
                                    }
                                }


                                // for error
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

                                Utility.dialog_error(Login_Activity.this, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());
                            dialog_bar.dismiss();
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

                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                params.put("email", email_txt);
                params.put("password", passwod_txt);
                params.put("token", refreshedToken);
                params.put("lang", sessionManager.getLang());
                params.put("source", "0");
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

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
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


                    social_email = account.getEmail();
                    //   email_Tv_edt.setText(social_email);

                    imageURL = String.valueOf(account.getPhotoUrl());
                }


                Glide.with(getApplicationContext()).load(imageURL).thumbnail(0.5f).animate(R.anim.from_down)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(profile_img);


                login_Auth_Social();


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

    }


    private void setProfileToView(JSONObject jsonObject) {
        try {

            social_email = jsonObject.getString("email");

            social_user_name = jsonObject.getString("name");

            userID = jsonObject.getString("id");

            social_id = userID;
            Log.d("userid", social_id);

            Log.v("EMAIL", social_email);

            login_Auth_Social();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void login_Auth_Social() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "loginWithSocial",
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
                                String stat_type = stat_reg.getString("activation_str");

                                // this for user
                                switch (stat_type) {
                                    case "waiting_Active": {

                                        String reg_id = stat_reg.getString("reg_id");
                                        Intent activ_reg = new Intent(getApplicationContext(), Activition_Mobile_Activity.class);
                                        activ_reg.putExtra("REG_ID", reg_id);
                                        startActivity(activ_reg);


                                        //this for provider
                                        break;
                                    }
                                    case "waiting_accept": {


                                        StringBuilder s = new StringBuilder(150);
                                        String street;
                                        JSONArray st = obj.getJSONArray("Errors");
                                        for (int i = 0; i < st.length(); i++) {
                                            street = st.getString(i);
                                            s.append(street);
                                            s.append("\n");
                                            Log.i("teeest", s.toString());
                                            // loop and add it to array or arraylist
                                        }


                                        Utility.dialog_error(Login_Activity.this, s.toString());


                                        //this for all
                                        break;
                                    }
                                    case "active": {

                                        Intent activ_reg = new Intent(getApplicationContext(), MainActivity.class);
                                        activ_reg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(activ_reg);

                                        String userName = stat_reg.getString("name");
                                        String userPic = stat_reg.getString("img");
                                        String userEmail = stat_reg.getString("email");
                                        String userType = stat_reg.getString("type");
                                        String userID = stat_reg.getString("id");
                                        String userAddress = stat_reg.getString("address");

                                        String lat = stat_reg.getString("lat");
                                        String lng = stat_reg.getString("lng");

                                        sessionManager.createLoginSession(userName, "0", userPic, userEmail, userType, userID, userAddress, lat, lng);

                                        Toast.makeText(getApplicationContext(), R.string.succ_login, Toast.LENGTH_LONG).show();

                                        //this for all
                                        break;
                                    }
                                    case "deactivated": {


                                        StringBuilder s = new StringBuilder(150);
                                        String street;
                                        JSONArray st = obj.getJSONArray("Errors");
                                        for (int i = 0; i < st.length(); i++) {
                                            street = st.getString(i);
                                            s.append(street);
                                            s.append("\n");
                                            Log.i("teeest", s.toString());
                                            // loop and add it to array or arraylist
                                        }


                                        Utility.dialog_error(Login_Activity.this, s.toString());

                                        break;
                                    }
                                }

                                dialog_bar.dismiss();
                                // for error
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

                                Utility.dialog_error(Login_Activity.this, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());
                            dialog_bar.dismiss();
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

                Map<String, String> params = new HashMap<>();

                params.put("social_id", social_id);
                params.put("userType", sessionManager.getUser_Type());
                params.put("lang", sessionManager.getLang());
                params.put("token", refreshedToken);
                params.put("source", "0");
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

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
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
