package com.redray.khadoomhome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.redray.khadoomhome.all_users.Activites.Account_Type;
import com.redray.khadoomhome.all_users.Activites.Lang_Choose_activity;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.TypewriterView;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash_Activity extends AppCompatActivity {

    ImageView imageView;

    Animation animZoomIn;


    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        sessionManager = new SessionManager(getApplicationContext());


        String languageToLoad  = sessionManager.getLang(); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        sessionManager.set_user_lang(languageToLoad);


        TextView splash_wel = findViewById(R.id.splash_welcome);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        splash_wel.startAnimation(fadeIn);
        fadeIn.setStartOffset(2000);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);





        imageView = findViewById(R.id.splash_circle);

        // load the animation
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        imageView.startAnimation(animZoomIn);

        // Create shake effect from xml resource
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        // View element to be shaken
        ImageView s = findViewById(R.id.splash_rectangle);
        // Perform animation
        s.startAnimation(shake);




        final TypewriterView typewriterView = findViewById(R.id.splash_about);
      //  typewriterView.setText(getString(R.string.app_name));
            typewriterView.pause(1500).type(" "+ getString(R.string.splash_describ)).pause()
                .run(new Runnable() {
                    @Override
                    public void run() {
                        // Finalize the text if user fiddled with it during animation.
                        typewriterView.setText(getString(R.string.splash_describ));
                        typewriterView.setEnabled(false);

                        
                    }
                });


        int SPLASH_DISPLAY_LENGTH = 4000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                boolean isFirstTime = SessionManager.isFirst(Splash_Activity.this);

                if (isFirstTime)
                {
                  /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash_Activity.this,Lang_Choose_activity.class);
                    Splash_Activity.this.startActivity(mainIntent);
                    Splash_Activity.this.finish();

                }else {


                    if (sessionManager.isLoggedIn())
                    {
                         /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(Splash_Activity.this,MainActivity.class);
                        Splash_Activity.this.startActivity(mainIntent);
                        Splash_Activity.this.finish();

                    }else {

                         /* Create an Intent that will start the login-Activity. */
                        Intent mainIntent = new Intent(Splash_Activity.this,Account_Type.class);
                        Splash_Activity.this.startActivity(mainIntent);
                        Splash_Activity.this.finish();

                    }

                }

            }
        }, SPLASH_DISPLAY_LENGTH);

    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
