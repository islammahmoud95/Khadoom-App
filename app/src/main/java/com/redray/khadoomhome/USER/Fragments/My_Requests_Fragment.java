package com.redray.khadoomhome.USER.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.redray.khadoomhome.USER.Adapters.Get_My_Requests;
import com.redray.khadoomhome.USER.Models.Get_Requests_Items;
import com.redray.khadoomhome.USER.Parser.ParseGet_Requests;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class My_Requests_Fragment extends Fragment {


    RecyclerView recyclerView;
    LinearLayoutManager manager;

    KProgressHUD dialog_bar;

    Get_My_Requests productsAdapter;

    SessionManager session;

    EditText filter_edt;

    public My_Requests_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);


        session = new SessionManager(getActivity());

        dialog_bar =  KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);




        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView =  view.findViewById(R.id.requests_recycle);



        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
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


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);



        filter_edt = view.findViewById(R.id.filter_edt);
        filter_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                filter(cs.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });



        return view;
    }



    void filter(String text){
        ArrayList<Get_Requests_Items> search_adapter = new ArrayList<>();
        for(Get_Requests_Items item_adapter: productsArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(item_adapter.getRequest_Status().toLowerCase().contains(text)){
                search_adapter.add(item_adapter);
            }else if (item_adapter.getRequest_Name().toLowerCase().contains(text)){
                search_adapter.add(item_adapter);
            }else if (item_adapter.getMain_service().toLowerCase().contains(text)) {
                search_adapter.add(item_adapter);
            }
        }
        //update recyclerview
        if (productsAdapter !=null)
        {
            productsAdapter.updateList(search_adapter);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "1");



        if (Check_Con.getInstance(getActivity()).isOnline()) {

            Get_My_list_Requests();
            dialog_bar.show();


        }else {

            Snackbar snackbar = Snackbar
                    .make(recyclerView, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();

        }

    }




    public void Get_My_list_Requests() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "users/getMyOrders",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("respooo22", response);

                        try {


                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status)
                            {
                                dialog_bar.dismiss();


                                showProducts(response);



                            }else
                            {

                                StringBuilder s = new StringBuilder(100);
                                String street;
                                JSONArray st = obj.getJSONArray("Errors");
                                for(int i=0;i<st.length();i++)
                                {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest",s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(getActivity(),s.toString());

                            }




                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }



                        dialog_bar.dismiss();

                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog_bar.dismiss();

                        Toast.makeText(getActivity(), R.string.server_down,Toast.LENGTH_LONG).show();

                        Log.e("error",error.getMessage() + "");
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();


                Log.d("ProductsParams", params.toString());

                return params;
            }


            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login",responseHeaders.get("Set-Cookie"));
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }



    private void showProducts(String json) {

        ParseGet_Requests parseProducts;
        parseProducts = new ParseGet_Requests(json);

        productsArrayList= parseProducts.parseProducts();

//        if (!productsArrayList.isEmpty()) {
        productsAdapter = new Get_My_Requests(getActivity(), productsArrayList);
        recyclerView.setAdapter(productsAdapter);
//            productsArrayList.clear();

        //   } else {
        productsAdapter.notifyDataSetChanged();
        //     }
    }

    ArrayList<Get_Requests_Items> productsArrayList = new ArrayList<>();


}
