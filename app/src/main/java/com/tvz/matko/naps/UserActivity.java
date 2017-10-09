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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    EditText name;
    Spinner age;
    Spinner sex;
    //RadioButton male;
    //RadioButton female;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen, removes upper status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user);


        //Populate spinner with numbers
        List ageList = new ArrayList<Integer>();
        ageList.add(0,"Age");
        for (int i = 1; i <= 100; i++) {
            ageList.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ageList);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        //adding years to age spinner
        age = (Spinner)findViewById(R.id.spinnerAge);
        age.setAdapter(spinnerArrayAdapter);

        //Populate spinner with genders
        List sexList = new ArrayList();
        sexList.add(0,"Gender");
        sexList.add(1,"Male");
        sexList.add(2,"Female");

        ArrayAdapter<Integer> spinnerGenderAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, sexList);
        spinnerGenderAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        //adding years to age spinner
        sex = (Spinner)findViewById(R.id.spinnerSex);
        sex.setAdapter(spinnerGenderAdapter);


        start = (Button) findViewById(R.id.buttonStartUser);
        name = (EditText) findViewById(R.id.editTextName);

        sex = (Spinner)findViewById(R.id.spinnerSex);

        //male = (RadioButton) findViewById(R.id.radioButtonMale);
        //female = (RadioButton) findViewById(R.id.radioButtonFemale);


        //When user presses start button app checks if user provided all of the info
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Checking network connection
                if(!isNetworkAvailable())
                {
                    Toast.makeText(UserActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();

                }
                //Checking if Name is entered
                else if(name.getText().toString().equals("")){
                    Toast.makeText(UserActivity.this, R.string.empty_name, Toast.LENGTH_LONG).show();
                }

                //Checking if Age is entered
                else if(age.getItemAtPosition(age.getSelectedItemPosition()).toString().equals("Age")){
                    Toast.makeText(UserActivity.this, R.string.empty_age, Toast.LENGTH_LONG).show();
                }

                //Checking if Age is entered
                else if(sex.getItemAtPosition(sex.getSelectedItemPosition()).toString().equals("Gender")){
                    Toast.makeText(UserActivity.this, R.string.empty_sex, Toast.LENGTH_LONG).show();
                }

                else{
                    //When button About is clicked, go to About activity
                    startActivity(new Intent(UserActivity.this, PictureActivity.class));

                }


            }
        });


    }


    //Checks if the network adapter is enabled
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
