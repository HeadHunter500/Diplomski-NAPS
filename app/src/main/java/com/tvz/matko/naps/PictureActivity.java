package com.tvz.matko.naps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    ConstraintLayout background;

    ImageView image, loading;

    Spinner valence, arousal;

    ImageButton valenceMin, valenceMax, arousalMin, arousalMax;

    Button next;


    Integer TempValence, TempArousal;

    String line = null;
    int code;

    List<String> pictures = new ArrayList<>();

    int activePic;

    int counter = 0;

    // url to create get all active pictures
    private static String ServerGetPictures = "http://lqovz8nye-site.etempurl.com/scripts/active_pic.php";
    //url to write the rating for each picture into the database
    private static String ServerRating = "http://lqovz8nye-site.etempurl.com/scripts/add_rating.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture);


        background = (ConstraintLayout)findViewById(R.id.background);

        image = (ImageView)findViewById(R.id.picture);
        loading = (ImageView)findViewById(R.id.imageViewLoading);

        valence = (Spinner)findViewById(R.id.spinnerValence);
        arousal = (Spinner)findViewById(R.id.spinnerArousal);

        valenceMin = (ImageButton)findViewById(R.id.imageButtonValenceMin);
        valenceMax = (ImageButton)findViewById(R.id.imageButtonValenceMax);

        arousalMin = (ImageButton)findViewById(R.id.imageButtonArousalMin);
        arousalMax = (ImageButton)findViewById(R.id.imageButtonArousalMax);

        next = (Button)findViewById(R.id.buttonNext);



        //Populate spinner with numbers
        List rateValence = new ArrayList<Integer>();
        rateValence.add(0, "Valence");
        for (int i = 1; i <= 9; i++) {
            rateValence.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapterValence = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, rateValence);
        spinnerArrayAdapterValence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adding numbers to valence spinner
        valence.setAdapter(spinnerArrayAdapterValence);


        //Populate spinner with numbers
        List rateArousal = new ArrayList<Integer>();
        rateArousal.add(0, "Arousal");
        for (int i = 1; i <= 9; i++) {
            rateArousal.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapterArousal = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, rateArousal);
        spinnerArrayAdapterArousal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adding numbers to arousal spinner
        arousal.setAdapter(spinnerArrayAdapterArousal);




        //gets the number of active pictures from UserActivity into this one
        Intent intent = getIntent();
        activePic = intent.getExtras().getInt("activePic");


            new ActivePictures().execute();




        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GetData();

                //Checking network connection
                if (!isNetworkAvailable()) {
                    Toast.makeText(PictureActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();

                }

                //Checking if Valence is entered
                else if (valence.getItemAtPosition(TempValence).toString().equals("Valence")) {
                    Toast.makeText(PictureActivity.this, R.string.empty_valence, Toast.LENGTH_LONG).show();
                }

                //Checking if Arousal is entered
                else if (arousal.getItemAtPosition(TempArousal).toString().equals("Arousal")) {
                    Toast.makeText(PictureActivity.this, R.string.empty_arousal, Toast.LENGTH_LONG).show();
                }

                else if(counter == (activePic-1)){

                    new RatePicture().execute();

                    Toast.makeText(PictureActivity.this, R.string.thanks, Toast.LENGTH_LONG).show();

                    finish();
                }

                else {

                    new RatePicture().execute();

                    // set the color teal first and hide all elements on the screen

                    background.setBackgroundColor(getResources().getColor(R.color.teal));

                    image.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);

                    valence.setVisibility(View.INVISIBLE);

                    arousal.setVisibility(View.INVISIBLE);

                    valenceMin.setVisibility(View.INVISIBLE);
                    valenceMax.setVisibility(View.INVISIBLE);

                    arousalMin.setVisibility(View.INVISIBLE);
                    arousalMax.setVisibility(View.INVISIBLE);

                    next.setVisibility(View.INVISIBLE);





                    // change to original after 5 secs and make all elements visible again
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            background.setBackgroundColor(getResources().getColor(R.color.blue));

                            image.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.VISIBLE);

                            valence.setSelection(0);

                            arousal.setSelection(0);

                            valence.setVisibility(View.VISIBLE);

                            arousal.setVisibility(View.VISIBLE);

                            valenceMin.setVisibility(View.VISIBLE);
                            valenceMax.setVisibility(View.VISIBLE);

                            arousalMin.setVisibility(View.VISIBLE);
                            arousalMax.setVisibility(View.VISIBLE);

                            next.setVisibility(View.VISIBLE);

                        }
                    }, 5000);


                }


            }
        });


    }



    //Fullscreen
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();

        super.onWindowFocusChanged(hasFocus);

            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }


    public void GetData(){

       TempValence = valence.getSelectedItemPosition();
       TempArousal = arousal.getSelectedItemPosition();

    }





    class ActivePictures extends AsyncTask<String, String, Void> {

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
       }

        @Override

        protected Void doInBackground(String... params) {




            try {

                //Connecting to the server where the php script is and gets all the active pictures so they can be displayed
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ServerGetPictures);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection successful");

            } catch (Exception e) {

                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Wrong IP address",
                        Toast.LENGTH_LONG).show();

            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection successful");

            } catch (Exception e) {

                Log.e("Fail 2", e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {


            try {
                //Script is getting the active pictures from the database
                //JSON result is retrieved - 1 if everything is ok - 0 if something went wrong
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));

                if (code == 0) {
                    Toast.makeText(PictureActivity.this, json_data.getString("message") , Toast.LENGTH_LONG).show();

                } else {


                    for(int i=0; i<activePic; i++){
                        String x = Integer.toString(i);
                        pictures.add(json_data.getJSONObject(x).getString("url"));


                    }

                    //Displaying the picture from the database into the ImageView element on the app screen
                    Picasso.with(PictureActivity.this).load(pictures.get(counter)).into(image);

                }



            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }





    class RatePicture extends AsyncTask<String, String, Void> {



        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
        }

        @Override
        //Rating of the picture
        protected Void doInBackground(String... params) {

            ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();

            NameValuePairs.add(new BasicNameValuePair("url", pictures.get(counter)));
            NameValuePairs.add(new BasicNameValuePair("valence", TempValence.toString()));
            NameValuePairs.add(new BasicNameValuePair("arousal", TempArousal.toString()));


            try {

                //Connecting to the server where the php script is which adds the rating for each picture into the database
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ServerRating);
                httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection successful");

            } catch (Exception e) {

                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Wrong IP address",
                        Toast.LENGTH_LONG).show();

            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection successful");

            } catch (Exception e) {

                Log.e("Fail 2", e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {


            try {
                //Script has added the new rate for a picture into the database
                //JSON result is retrieved - 1 if everything is ok - 0 if something went wrong
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));


                if (code == 0) {
                    Toast.makeText(PictureActivity.this, json_data.getString("message") , Toast.LENGTH_LONG).show();

                } else {

                    counter++;
                    Picasso.with(PictureActivity.this).load(pictures.get(counter)).into(image);
                }


            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }






    //Press twice the back button to exit
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
