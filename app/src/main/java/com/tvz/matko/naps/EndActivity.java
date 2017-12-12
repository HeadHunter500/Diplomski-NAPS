package com.tvz.matko.naps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class EndActivity extends AppCompatActivity {

    TextView result;
    Button end;

    /*
    int activePic;

    String[] pic_description = new String[activePic];
    String[] pic_valence = new String[activePic];
    String[] pic_arousal = new String[activePic];

    String[] user_valence = new String[activePic];
    String[] user_arousal = new String[activePic];
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen, removes upper status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_end);

        result = (TextView)findViewById(R.id.textViewResult);

        end = (Button)findViewById(R.id.buttonEnd);

        /*
        Intent intent = getIntent();
        activePic = intent.getExtras().getInt("activePic");

        pic_description = intent.getExtras().getStringArray("pic_description");
        pic_valence = intent.getExtras().getStringArray("pic_valence");
        pic_arousal = intent.getExtras().getStringArray("pic_arousal");

        user_valence = intent.getExtras().getStringArray("user_valence");
        user_arousal = intent.getExtras().getStringArray("user_arousal");

        result.setText(Arrays.toString(pic_description));
        */

        end.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EndActivity.this.finish();
                //System.exit(0);
            }
        });


    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    //Checks if the network adapter is enabled
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
