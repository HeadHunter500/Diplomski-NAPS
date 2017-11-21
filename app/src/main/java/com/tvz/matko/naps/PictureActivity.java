package com.tvz.matko.naps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PictureActivity extends AppCompatActivity {

    ImageView image;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        image = (ImageView)findViewById(R.id.pic);

        next = (Button)findViewById(R.id.buttonNext);

        Picasso.with(this).load("http://lqovz8nye-site.etempurl.com/images/Animals_002_v.jpg").into(image);





        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Picasso.with(PictureActivity.this).load("http://lqovz8nye-site.etempurl.com/images/Animals_166_v.jpg").into(image);

            }
        });
    }



    //Fullscreen

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



    //_____________




    //_______

}
