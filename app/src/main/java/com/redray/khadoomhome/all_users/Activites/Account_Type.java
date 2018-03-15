package com.redray.khadoomhome.all_users.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.redray.khadoomhome.R;
import com.redray.khadoomhome.utilities.SessionManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Account_Type extends AppCompatActivity {

    Button provider_btn , user_btn , technical_btn;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        sessionManager = new SessionManager(getApplicationContext());

        user_btn = findViewById(R.id.user_apply_btn);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.setUser_Type("1");

                Intent select_type = new Intent(getApplicationContext(), Login_Activity.class);

                startActivity(select_type);


            }
        });

        provider_btn = findViewById(R.id.provider_apply_btn);
        provider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.setUser_Type("2");

                Intent select_type = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(select_type);

            }
        });

        technical_btn = findViewById(R.id.technical_type_btn);
        technical_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.setUser_Type("3");

                Intent select_type = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(select_type);

            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
