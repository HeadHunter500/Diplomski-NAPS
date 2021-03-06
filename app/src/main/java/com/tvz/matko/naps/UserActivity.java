package com.tvz.matko.naps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import android.provider.Settings.Secure;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Object;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.provider.Settings.Secure;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.content.Context;

public class UserActivity extends AppCompatActivity {


    EditText name;
    Spinner age;
    Spinner sex;

    Button start;

    String TempName;
    Integer TempAge;
    Integer TempSex;


    // Url to php script which adds new user to the database
    private static String ServerURL = "http://52.234.151.31/naps/scripts/add_user.php";

    String line = null;
    int code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen, removes upper status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_user);

        //EditText
        name = (EditText) findViewById(R.id.editTextName);
        //Spinners
        age = (Spinner) findViewById(R.id.spinnerAge);
        sex = (Spinner) findViewById(R.id.spinnerSex);
        //Button
        start = (Button) findViewById(R.id.buttonStartUser);



        //Populate spinner with numbers
        List ageList = new ArrayList<Integer>();
        ageList.add(0, "Age");
        for (int i = 1; i <= 100; i++) {
            ageList.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ageList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adding years to age spinner
        age.setAdapter(spinnerArrayAdapter);




        //Populate spinner with genders
        List sexList = new ArrayList();
        sexList.add(0, "Gender");
        sexList.add(1, "Male");
        sexList.add(2, "Female");

        ArrayAdapter<Integer> spinnerGenderAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, sexList);
        spinnerGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adding genders to gender spinner
        sex.setAdapter(spinnerGenderAdapter);







        //When user presses start button app checks if user provided all of the info
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GetData();


                //Checking network connection
                if (!isNetworkAvailable()) {
                    Toast.makeText(UserActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();

                }
                //Checking if Name is entered
                else if (TempName.equals("")) {
                    Toast.makeText(UserActivity.this, R.string.empty_name, Toast.LENGTH_LONG).show();
                }

                //Checking if Age is entered
                else if (age.getItemAtPosition(TempAge).toString().equals("Age")) {
                    Toast.makeText(UserActivity.this, R.string.empty_age, Toast.LENGTH_LONG).show();
                }

                //Checking if Gender is entered
                else if (sex.getItemAtPosition(TempSex).toString().equals("Gender")) {
                    Toast.makeText(UserActivity.this, R.string.empty_sex, Toast.LENGTH_LONG).show();
                } else {

                  //If all the data has been provided by the user, then start adding them into the database and get the picture sequence and
                  //start the next activity
                  new UserStart().execute();


                }

            }
        });


    }

    public void GetData(){

        TempName = name.getText().toString();

        TempAge = age.getSelectedItemPosition();

        TempSex = sex.getSelectedItemPosition();

    }






    class UserStart extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {

            //Dialog which shows loading before picture sequence starts
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    UserStart.this.cancel(true);
                }
            });

        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();

            NameValuePairs.add(new BasicNameValuePair("name", TempName));
            NameValuePairs.add(new BasicNameValuePair("age", TempAge.toString()));
            NameValuePairs.add(new BasicNameValuePair("sex", TempSex.toString()));




            try {

                //Connecting to the server where the php script is which adds new user to the database
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ServerURL);
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
                //Script has added the new user into the database
                //JSON result is retrieved - 1 if everything is ok - 0 if something went wrong
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));


                if (code == 0) {
                    Toast.makeText(UserActivity.this, json_data.getString("message") , Toast.LENGTH_LONG).show();

                } else {

                    int activePic = json_data.getInt("num");

                    //Forwards number of active pictures to the PictureActivity and starts that activity
                    Intent qq = new Intent(UserActivity.this,PictureActivity.class);
                    qq.putExtra("activePic",activePic);
                    startActivity(qq);

                    finish();


                }

                this.progressDialog.dismiss();

            } catch (Exception e) {

                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }





    //Checks if the network adapter is enabled
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
