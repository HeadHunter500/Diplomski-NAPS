package com.tvz.matko.naps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen, removes upper status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);





        //When button About is clicked, go to About activity
        Button about = (Button) findViewById(R.id.buttonAbout);

        about.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        //When button About is clicked, go to About activity
        Button start = (Button) findViewById(R.id.buttonStart);

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Checking network connection
                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();

                }

                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });

    }

    // FUllscreen
    /*
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

    */

    //Checks if the network adapter is enabled
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
