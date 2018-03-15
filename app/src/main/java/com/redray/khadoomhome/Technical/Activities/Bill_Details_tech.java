package com.redray.khadoomhome.Technical.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.redray.khadoomhome.Technical.Adapter.Add_Parts_adapter_tech;
import com.redray.khadoomhome.Technical.Adapter.Get_Parts_adapter_tech;
import com.redray.khadoomhome.Technical.DataTransferInterface_total_num;
import com.redray.khadoomhome.Technical.Model.Add_Parts_Items_tech;
import com.redray.khadoomhome.Technical.Model.Get_Parts_Items_tech;
import com.redray.khadoomhome.Technical.Parser.ParseGet_Parts_tech;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Bill_Details_tech extends AppCompatActivity implements DataTransferInterface_total_num {

    KProgressHUD dialog_bar;
    SessionManager sessionManager;
    NestedScrollView scroll_bill_lay;

    FloatingActionButton add_new_part;


    //get parts
    Get_Parts_adapter_tech productsAdapter_Parts;
    RecyclerView recyclerView_get_parts;
    LinearLayoutManager manager;
    LinearLayout parts_lay;
    RadioGroup radioGroup_order_Type;
    String parts_json;
    String sub_ser_id = "";


    //add new parts
    Add_Parts_adapter_tech productsAdapter_add_Parts;
    RecyclerView recyclerView_add_parts;
    LinearLayoutManager manager_add_parts;


    int ACTIVITY_MORE_PART = 50;


    EditText order_num_edt, bill_num_edt, visit_fees_edt, parts_fees_edt, addit_fees_edt, discount_edt;
    TextView full_total_bill, total_parts_txt;
    String order_ID;
    String bill_ID;


    String discount_value_str;

    Button calc_total_btn, done_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details_tech);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            order_ID = intent.getStringExtra("ORDER_ID");
            Log.d("order_ID", order_ID);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        scroll_bill_lay = findViewById(R.id.scroll_bill_lay);

        total_parts_txt = findViewById(R.id.total_parts_txt);
        order_num_edt = findViewById(R.id.order_num_edt);
        bill_num_edt = findViewById(R.id.bill_num_edt);
        visit_fees_edt = findViewById(R.id.visit_fees_edt);
        parts_fees_edt = findViewById(R.id.parts_fees_edt);
        addit_fees_edt = findViewById(R.id.addit_fees_edt);
        discount_edt = findViewById(R.id.discount_edt);
        full_total_bill = findViewById(R.id.full_total_bill);

        parts_lay = findViewById(R.id.parts_lay);

        radioGroup_order_Type = findViewById(R.id.radioGroup_order_Type);


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


        productsAdapter_add_Parts = new Add_Parts_adapter_tech(this, productsArrayList_add_Parts, this);
        manager_add_parts = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_add_parts = findViewById(R.id.add_parts_recycle);
        recyclerView_add_parts.setLayoutManager(manager_add_parts);
        recyclerView_add_parts.setAdapter(new RecyclerView.Adapter() {
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


        recyclerView_add_parts.setHasFixedSize(true);
        recyclerView_add_parts.setNestedScrollingEnabled(true);


        add_new_part = findViewById(R.id.add_new_part);
        add_new_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent add_part_intent = new Intent(getApplicationContext(), Add_Additional_Part.class);
                startActivityForResult(add_part_intent, ACTIVITY_MORE_PART);

            }
        });


        visit_fees_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (visit_fees_edt.getText().toString().trim().equals("")) {
                    visit_fees_edt.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        calc_total_btn = findViewById(R.id.calc_total_btn);
        calc_total_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int visit_value = Integer.valueOf(visit_fees_edt.getText().toString().trim());

                if (discount_edt.getText().toString().trim().equals("0") || TextUtils.isEmpty(discount_edt.getText().toString())) {

                    full_total_bill.setText(getString(R.string.total) + " " + String.valueOf(sum_additional_parts + sum_parts + visit_value));

                } else {

                    int discount_value = Integer.valueOf(discount_edt.getText().toString().trim());

                    int percentage = ((sum_additional_parts + sum_parts + visit_value) * discount_value) / 100;

                    int result = (sum_additional_parts + sum_parts + visit_value) - percentage;


                    Log.d("dfsd", String.valueOf(result));
                    full_total_bill.setText(getString(R.string.total) + " " + result);


                }

            }

        });


        done_btn = findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {


                    if (validate()) {

                        dialog_bar.show();

                        edit_Bill_Details();

                    }

                } else {


                    Snackbar snackbar = Snackbar
                            .make(scroll_bill_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
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


        if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

            dialog_bar.show();

            get_Bill_Details();


        } else {


            Snackbar snackbar = Snackbar
                    .make(scroll_bill_lay, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            snackbar.show();
        }


    }


    public void get_Bill_Details() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "tech/getInvoiceDetailsTech",
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
                                String num_order_str = stat_reg.getString("order_id");
                                String bill_num_str = stat_reg.getString("bill_id");
                                String visit_fees_str = stat_reg.getString("visit_fees");
                                String parts_fees_str = stat_reg.getString("parts_fees");
                                String addit_fees_str = stat_reg.getString("additional_work_fees");
                                String total_bill_str = stat_reg.getString("total");

                                bill_ID = bill_num_str;
                                order_num_edt.setText(num_order_str);
                                bill_num_edt.setText(bill_num_str);
                                visit_fees_edt.setText(visit_fees_str);
                                parts_fees_edt.setText(parts_fees_str);
                                addit_fees_edt.setText(addit_fees_str);
                                full_total_bill.setText(getString(R.string.total) + " " + total_bill_str);


                                sub_ser_id = stat_reg.getString("sub_cat_id");

                                parts_json = String.valueOf(stat_reg);  // this data parts


                                showProducts_edit(parts_json);


//                                additional = stat_reg.getString("additional_parts");
//                                if (additional.equals("1"))
//                                {
//                                    showProducts_edit(parts_json);
//                                    //  parts_lay.setVisibility(View.VISIBLE);
//
//                                }else if (additional.equals("0")){

                                // parts_lay.setVisibility(View.VISIBLE);
//                                    dialog_bar.show();
//                                }


                                dialog_bar.dismiss();


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

                                Utility.dialog_error(Bill_Details_tech.this, s.toString());

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

                //   params.put("lang", sessionManager.getLang());
                params.put("order_id", order_ID);

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


    private void showProducts_edit(String json) {

        ParseGet_Parts_tech parseProducts;
        parseProducts = new ParseGet_Parts_tech(json);

        productsArrayList_Parts = parseProducts.parseProducts();

        //    if (!productsArrayList_Parts.isEmpty()) {

        productsAdapter_Parts = new Get_Parts_adapter_tech(this, productsArrayList_Parts, this);

        recyclerView_get_parts.setAdapter(productsAdapter_Parts);

        //   } else {
        productsAdapter_Parts.notifyDataSetChanged();
        //   }

    }

    ArrayList<Get_Parts_Items_tech> productsArrayList_Parts = new ArrayList<>();

    ArrayList<Add_Parts_Items_tech> productsArrayList_add_Parts = new ArrayList<>();


    int sum_parts;

    @Override
    public void addValues_total_num(int sum) {
        sum_parts = sum;
        total_parts_txt.setText(String.valueOf(+sum));
        Log.d("SUM IS ", String.valueOf(+sum));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_MORE_PART) {
            if (resultCode == Activity.RESULT_OK) {


                String ID = data.getExtras().getString("ID");
                String TOTAL_UNIT_COST = data.getExtras().getString("TOTAL_UNIT_COST");
                String UNIT_COST = data.getExtras().getString("UNIT_COST");
                String QUANTITY = data.getExtras().getString("QUANTITY");
                String PART_NAME = data.getExtras().getString("PART_NAME");
//                String CHECKBOX = data.getExtras().getString("CHECKBOX");


                Add_Parts_Items_tech add_parts_items_tech = new Add_Parts_Items_tech();

                add_parts_items_tech.setProductId(ID);
                add_parts_items_tech.setTotal_unit_Cost(TOTAL_UNIT_COST);
                add_parts_items_tech.setUnit_Cost(UNIT_COST);
                add_parts_items_tech.setQuantity(QUANTITY);
                add_parts_items_tech.setProductName(PART_NAME);
                add_parts_items_tech.setcheckbox(true);

                productsArrayList_add_Parts.add(add_parts_items_tech);

                recyclerView_add_parts.setAdapter(productsAdapter_add_Parts);
                productsAdapter_add_Parts.notifyDataSetChanged();


                addit_fees_edt.setText(String.valueOf(+getSum()));
            }
        }
    }

    private int getSum() {
        int sum = 0;
        for (Add_Parts_Items_tech obj : productsArrayList_add_Parts) {
            if (obj.getcheckbox()) {

                float quant = Integer.parseInt(obj.getQuantity());
                float cost = Integer.parseInt(obj.getUnit_Cost());

                sum += quant * cost;
            }
        }

        // this to get sum from add additional part
        sum_additional_parts = sum;

        //  recyclerView_add_parts.smoothScrollToPosition();
        return sum;
    }


    int sum_additional_parts;

    @Override
    public void addValues_additional_total_num(int sum) {

        // this to get sum from plus or minus additional part
        sum_additional_parts = sum;
        addit_fees_edt.setText(String.valueOf(+sum));

        Log.d("SUM addi IS ", String.valueOf(+sum));
    }


    // this for parts is any checked that's mean 1 if not 0
    String parts_stat;
    JSONArray jsonArray_parts;
    JSONArray jsonArray_additional_parts;


    public boolean validate() {



        jsonArray_parts = new JSONArray();
        jsonArray_additional_parts = new JSONArray();

        get_Parts_Json();
        get_additional_Parts_Json();


        // required
        discount_value_str = discount_edt.getText().toString().trim();


        if (TextUtils.isEmpty(discount_value_str)) {
            discount_edt.setText("");
            discount_value_str = "";
        }


        return true;
    }


    public void get_Parts_Json() {

        int childCount = recyclerView_get_parts.getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (recyclerView_get_parts.findViewHolderForLayoutPosition(i) instanceof Get_Parts_adapter_tech.RecyclerViewHolder) {

                try {
                    JSONObject jsonObject = new JSONObject();

                    if (productsAdapter_Parts.productsArrayList.get(i).getcheckbox()) {

                        jsonObject.put("part_id", productsAdapter_Parts.productsArrayList.get(i).getProductId());
                        jsonObject.put("qty", productsAdapter_Parts.productsArrayList.get(i).getQuantity());
                        jsonObject.put("new_cost", productsAdapter_Parts.productsArrayList.get(i).getUnit_Cost());

                        jsonArray_parts.put(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        if (jsonArray_parts.length() > 0) {
            Log.d("printAllEditTextValues", jsonArray_parts.toString());
            // here that's mean has parts cause have checked items
            parts_stat = "1";
        } else {

            parts_stat = "0";
        }

        Log.e("printAllEditTextValues", parts_stat);
        Log.e("printAllEditTextValues", jsonArray_parts.toString());


    }


    public void get_additional_Parts_Json() {

        int childCount = recyclerView_add_parts.getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (recyclerView_add_parts.findViewHolderForLayoutPosition(i) instanceof Add_Parts_adapter_tech.RecyclerViewHolder) {

                try {
                    JSONObject jsonObject = new JSONObject();

                    //   if (productsAdapter_add_Parts.productsArrayList.get(i).getcheckbox()) {

                    jsonObject.put("content", productsAdapter_add_Parts.productsArrayList.get(i).getProductName());
                    jsonObject.put("qty", productsAdapter_add_Parts.productsArrayList.get(i).getQuantity());
                    jsonObject.put("cost", productsAdapter_add_Parts.productsArrayList.get(i).getUnit_Cost());

                    jsonArray_additional_parts.put(jsonObject);
                    //    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        Log.e("printAll_additinal", jsonArray_additional_parts.toString());


    }


    //this method to edit order or bill details parts and close order done (finished)
    public void edit_Bill_Details() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "tech/postTechInvoice",
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

                                Snackbar snackbar = Snackbar
                                        .make(scroll_bill_lay, "Operation Finished Successfully", Snackbar.LENGTH_LONG);

                                snackbar.show();


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

                                Utility.dialog_error(Bill_Details_tech.this, s.toString());

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

                //   params.put("lang", sessionManager.getLang());
                params.put("invoice_id", bill_ID);
                params.put("visit_fees", visit_fees_edt.getText().toString());
                params.put("additional_parts", parts_stat);
                params.put("parts", String.valueOf(jsonArray_parts));
                params.put("additional_work", String.valueOf(jsonArray_additional_parts));
                params.put("discount", discount_value_str);

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