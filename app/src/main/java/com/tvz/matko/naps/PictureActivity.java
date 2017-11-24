package com.tvz.matko.naps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    ImageView image;

    Button next;

    String line = null;
    int code;

    String pictureURL = "";



    // url to create new product
    private static String ServerURL = "http://lqovz8nye-site.etempurl.com/scripts/active_pic.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);



        image = (ImageView)findViewById(R.id.pic);

        next = (Button)findViewById(R.id.buttonNext);





        Picasso.with(this).load("http://lqovz8nye-site.etempurl.com/images/Animals_002_v.jpg").into(image);

        //gets the number of active pictures from UserActivity into this one
        Intent intent = getIntent();
        int activePic = intent.getExtras().getInt("activePic");


        new ActivePictures().execute();



        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Toast.makeText(PictureActivity.this, url, Toast.LENGTH_LONG).show();

                Picasso.with(PictureActivity.this).load(pictureURL).into(image);

            }
        });



        //System.out.println(Arrays.toString(pictures.toArray()));
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
                HttpPost httppost = new HttpPost(ServerURL);
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
                    pictureURL = json_data.getJSONObject("2").getString("url");

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
                }



            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }


//_____________
}
