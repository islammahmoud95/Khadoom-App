package com.redray.khadoomhome.all_users.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.redray.khadoomhome.MainActivity;
import com.redray.khadoomhome.R;

public class Genral_Notify_detail_activ extends AppCompatActivity {


    String  msg_Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.activity_genral_notify_detail_activ);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Intent main_intent = getIntent();
        if (main_intent != null) {
            msg_Content = main_intent.getStringExtra("MSG_CONTENT");

        }


        TextView details_genral_notify = findViewById(R.id.details_genral_notify);

        details_genral_notify.setText(msg_Content);


        Button close_btn =  findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
