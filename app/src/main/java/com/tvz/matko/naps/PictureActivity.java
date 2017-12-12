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
import android.view.Window;
import android.view.WindowManager;
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
/*
    List<String> pic_description = new ArrayList<>();
    List<String> pic_valence = new ArrayList<>();
    List<String> pic_arousal = new ArrayList<>();
    */

    int activePic;

    /*
    List<String> user_valence = new ArrayList<>();
    List<String> user_arousal = new ArrayList<>();
    */

    int counter = 0;

    // url to create get all active pictures
    private static String ServerGetPictures = "http://lqovz8nye-site.etempurl.com/scripts/active_pic.php";
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
        //adding years to age spinner
        valence.setAdapter(spinnerArrayAdapterValence);


        //Populate spinner with numbers
        List rateArousal = new ArrayList<Integer>();
        rateArousal.add(0, "Arousal");
        for (int i = 1; i <= 9; i++) {
            rateArousal.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapterArousal = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, rateArousal);
        spinnerArrayAdapterArousal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adding years to age spinner
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

                    //from list to string[] so it can be passed to EndActivity
                    /*
                    String[] temp_pic_description = new String[activePic];
                    String[] temp_pic_valence = new String[activePic];
                    String[] temp_pic_arousal = new String[activePic];

                    String[] temp_user_valence = new String[activePic];
                    String[] temp_user_arousal = new String[activePic];

                    for(int i=0; i<activePic; i++){
                        temp_pic_description[i] = pic_description.get(i);
                        temp_pic_valence[i] = pic_valence.get(i);
                        temp_pic_arousal[i] = pic_arousal.get(i);

                        temp_user_valence[i] = user_valence.get(i);
                        temp_user_arousal[i] = user_arousal.get(i);
                    }
                    */


                    //Intent qq = new Intent(PictureActivity.this,EndActivity.class);


                    /*
                    qq.putExtra("pic_description",temp_pic_description);
                    qq.putExtra("pic_valence",temp_pic_valence);
                    qq.putExtra("pic_arousal",temp_pic_arousal);

                    qq.putExtra("user_valence",temp_user_valence);
                    qq.putExtra("user_arousal",temp_user_arousal);

                    qq.putExtra("activePic",activePic);
                    */

                    //startActivity(qq);

                    Toast.makeText(PictureActivity.this, R.string.thanks, Toast.LENGTH_LONG).show();

                    finish();
                }

                else {

                    new RatePicture().execute();

                    // set the color teal first.

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





                    // change to original after 5 secs.
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


    //_____________


    class ActivePictures extends AsyncTask<String, String, Void> {

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
       }

        @Override
        //Spajanje na skriptu koja se spaja na tablicu korisnika i izlistava ih
        protected Void doInBackground(String... params) {




            try {

                //Spajanje na server gdje se nalazi skripta koja unosi korisnika u bazu podataka
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ServerGetPictures);
                //httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "konekcija uspješna ");

            } catch (Exception e) {

                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Pogrešna IP Adresa",
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
                Log.e("pass 2", "konekcija uspješna ");

            } catch (Exception e) {

                Log.e("Fail 2", e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {


            try {
                //Skripta je ubacila korisnika u bazu podataka
                //vraća se pomoću JSON-a rezultat 1 ako je sve prošlo ok ili 0 ako je registracija bila neuspješna
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));

                if (code == 0) {
                    Toast.makeText(PictureActivity.this, json_data.getString("message") , Toast.LENGTH_LONG).show();

                } else {



                    //Uzeo url od slike ---proba
                    //pictureURL = json_data.getJSONObject("2").getString("url");

                    for(int i=0; i<activePic; i++){
                        String x = Integer.toString(i);
                        pictures.add(json_data.getJSONObject(x).getString("url"));
                        /*
                        pic_description.add(json_data.getJSONObject(x).getString("description"));
                        pic_valence.add(json_data.getJSONObject(x).getString("valence"));
                        pic_arousal.add(json_data.getJSONObject(x).getString("arousal"));
                        */

                    }

                    Picasso.with(PictureActivity.this).load(pictures.get(counter)).into(image);


                    //Toast.makeText(PictureActivity.this, dick, Toast.LENGTH_LONG).show();

                    /*
                    ArrayList<String> pictures = new ArrayList<String>();

                    JSONArray Jarray = new JSONArray(result);
                    //Pomoću for petlje se prolazi kroz sve uplate
                    for (int i = 0; i < Jarray.length(); i++) {


                        JSONObject Jasonobject = null;

                        Jasonobject = Jarray.getJSONObject(i);

                        String id = Jasonobject.getString("id");
                        String url = Jasonobject.getString("url");
                        //pictures.add(id.toString());
                        //pictures.add(url.toString());

                        finish();
                    }

                    */
                }



            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }


//_____________



    //______________________


    class RatePicture extends AsyncTask<String, String, Void> {

        //private ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
/*
            //Dijalog koji prikazuje učitavanje, odnosno prijavljivanje
            //progressDialog.setMessage("Prijavljivanje...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    UserActivity.UserStart.this.cancel(true);

                }
            });
*/
        }

        @Override
        //rejta se
        protected Void doInBackground(String... params) {

            ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();

            NameValuePairs.add(new BasicNameValuePair("url", pictures.get(counter)));
            NameValuePairs.add(new BasicNameValuePair("valence", TempValence.toString()));
            NameValuePairs.add(new BasicNameValuePair("arousal", TempArousal.toString()));
            //NameValuePairs.add(new BasicNameValuePair("id_phone", deviceid));

            /*
            user_valence.add(TempValence.toString());
            user_arousal.add(TempArousal.toString());
            */

            try {

                //Spajanje na server gdje se nalazi skripta koja unosi korisnika u bazu podataka
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ServerRating);
                httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "konekcija uspješna ");

            } catch (Exception e) {

                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Pogrešna IP Adresa",
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
                Log.e("pass 2", "konekcija uspješna ");

            } catch (Exception e) {

                Log.e("Fail 2", e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {


            try {
                //Skripta je ubacila korisnika u bazu podataka
                //vraća se pomoću JSON-a rezultat 1 ako je sve prošlo ok ili 0 ako je registracija bila neuspješna
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));


                if (code == 0) {
                    Toast.makeText(PictureActivity.this, json_data.getString("message") , Toast.LENGTH_LONG).show();

                } else {

                    counter++;
                    Picasso.with(PictureActivity.this).load(pictures.get(counter)).into(image);
                }

                //this.progressDialog.dismiss();

            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }




    //_____________________



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
