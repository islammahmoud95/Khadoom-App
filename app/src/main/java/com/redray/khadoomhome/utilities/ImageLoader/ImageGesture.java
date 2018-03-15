package com.redray.khadoomhome.utilities.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.redray.khadoomhome.R;


public class ImageGesture extends Activity {
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_product_details);

        Intent intent = getIntent();


        TouchImageView image = findViewById(R.id.my_full_image);


        if (intent.getStringExtra("URL_OF_IMAGE") == null) {

            Glide.with(this).load(R.drawable.khadoom_logo).animate(R.anim.from_down).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(image);

            Log.d("imaaage", "empty");

        } else {

            url = intent.getStringExtra("URL_OF_IMAGE");


            Log.d("imaaage", url);


            ImageLoader imageLoader_menu = new ImageLoader(this);
            imageLoader_menu.DisplayImage(url, image);


            Glide.with(this).load(url).animate(R.anim.zoom_in).diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);


        }

    }

}
