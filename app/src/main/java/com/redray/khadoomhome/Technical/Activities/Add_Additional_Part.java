package com.redray.khadoomhome.Technical.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.SessionManager;

public class Add_Additional_Part extends AppCompatActivity {

    KProgressHUD dialog_bar;
    SessionManager sessionManager;

    LinearLayout main_add_part_lay;

    EditText name_part_edt,unit_cost_edt;
    TextView total_unit_cost_txt , quantity_txt;
    String name_part_str, quantity_str,unit_cost_str ,total_unit_cost_str;

    ImageButton min_qty_btn,plus_qty_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_add_aditinal_part);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        dialog_bar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);

        sessionManager = new SessionManager(getApplicationContext());

        main_add_part_lay = findViewById(R.id.main_add_part_lay);


        name_part_edt = findViewById(R.id.name_part_edt);
        quantity_txt = findViewById(R.id.quantity_txt);
        unit_cost_edt = findViewById(R.id.unit_cost_edt);
        total_unit_cost_txt = findViewById(R.id.total_unit_cost_txt);


        plus_qty_btn = findViewById(R.id.plus_qty_btn);
        min_qty_btn = findViewById(R.id.min_qty_btn);


        plus_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                unit_cost_str = unit_cost_edt.getText().toString();

                int quantityNum = Integer.parseInt(quantity_txt.getText().toString());
                if (quantityNum < 99) {
                    quantityNum++;
                }

                quantity_txt.setText(String.valueOf(quantityNum));

                if (unit_cost_edt.getText().toString().equals("")){
                    unit_cost_str = "0";
                }

                int unit_total_cost = (Integer.valueOf(quantity_txt.getText().toString()) * Integer.valueOf(unit_cost_str));
                total_unit_cost_txt.setText(String.valueOf(unit_total_cost));

            }


        });

        min_qty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unit_cost_str = unit_cost_edt.getText().toString();

                int quantityNum = Integer.parseInt(quantity_txt.getText().toString());
                quantityNum--;
                if (quantityNum == 0) {
                    quantityNum = 1;
                }

                quantity_txt.setText(String.valueOf(quantityNum));

                if (unit_cost_edt.getText().toString().equals("")){
                    unit_cost_str = "0";
                }

                int unit_total_cost = (Integer.valueOf(quantity_txt.getText().toString()) * Integer.valueOf(unit_cost_str));
                total_unit_cost_txt.setText(String.valueOf(unit_total_cost));


            }
        });



        unit_cost_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                // this block to calc unit total cost if unit cost not empty
                if (!unit_cost_edt.getText().toString().trim().equals("")){

                    int unit_total_cost = (Integer.valueOf(quantity_txt.getText().toString()) *
                            Integer.valueOf(unit_cost_edt.getText().toString()));

                    total_unit_cost_txt.setText(String.valueOf(unit_total_cost));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });






        Button add_part_btn = findViewById(R.id.add_part_btn);
        add_part_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validate()) {


                    Intent i = new Intent();
                    i.putExtra("ID","0");
                    i.putExtra("TOTAL_UNIT_COST",total_unit_cost_str);
                    i.putExtra("UNIT_COST",unit_cost_str);
                    i.putExtra("QUANTITY",quantity_str);
                    i.putExtra("PART_NAME",name_part_str);
                    i.putExtra("CHECKBOX",true);

                    setResult(Activity.RESULT_OK,i);
                    finish();

                }

            }
        });


        Button cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(unit_cost_edt.getWindowToken(), 0);
    }

    public boolean validate() {
        boolean valid = true;

        try {
            // required
            name_part_str = name_part_edt.getText().toString().trim();
            quantity_str = quantity_txt.getText().toString().trim();
            unit_cost_str = unit_cost_edt.getText().toString().trim();
            total_unit_cost_str = total_unit_cost_txt.getText().toString().trim();

        } catch (Exception e) {

            Log.e("test", e.getMessage());
        }


        if (name_part_str.isEmpty() ) {
            name_part_edt.setError("Part Name Must be Not Null");
            valid = false;
        } else {
            name_part_edt.setError(null);

        }

        if (quantity_str.isEmpty() ) {
            quantity_txt.setError("Part quantity Must be Not Null");
            valid = false;
        } else {
            quantity_txt.setError(null);

        }

        if (unit_cost_str.isEmpty() ) {
            unit_cost_edt.setError("Part cost Must be Not Null");
            valid = false;
        } else {
            unit_cost_edt.setError(null);

        }

        return valid;
    }

}
