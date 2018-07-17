package com.example.mark.mstutor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<HashMap<String,Object>> days = new ArrayList<>();
    Spinner daySpinner;
    Spinner hourSpinner;
    EditText hours;
    ImageButton delete;
    Button submit;
    SignupAdapter signupAdapter;

    private String DAY_SPINNER = "daySpinner";
    private String HOUR_SPINNER = "hourSpinner";
    private String HOURS = "hours";
    private String DELETE = "delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.signupRecycler);
        initializePage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                daySpinner = new Spinner(getBaseContext());
                hourSpinner = new Spinner(getBaseContext());
                hours = new EditText(getBaseContext());
                delete = new ImageButton(getBaseContext());

                HashMap<String,Object> map = new HashMap<>();
                map.put(DAY_SPINNER,daySpinner);
                map.put(HOUR_SPINNER,hourSpinner);
                map.put(HOURS,hours);
                map.put(DELETE,delete);

                signupAdapter.addDay(map);
                signupAdapter.notifyDataSetChanged();

            }
        });


        submit = findViewById(R.id.signupSubmitDays);

        submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeDuplicateDays(signupAdapter.details);
            Log.d("SUBMITTING", signupAdapter.details.toString());
        }
    });


    }

    public void initializePage(){

        daySpinner = new Spinner(getBaseContext());
        hourSpinner = new Spinner(getBaseContext());
        hours = new EditText(getBaseContext());
        delete = new ImageButton(getBaseContext());
        submit = findViewById(R.id.signupSubmitDays);

        HashMap<String,Object> map = new HashMap<>();
        map.put(DAY_SPINNER,daySpinner);
        map.put(HOUR_SPINNER,hourSpinner);
        map.put(HOURS,hours);
        map.put(DELETE,delete);
        days.add(map);
        signupAdapter = new SignupAdapter(getBaseContext(),days);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(signupAdapter);


    }

    public void removeDuplicateDays(ArrayList<HashMap<String,Object>> arr){

        for(int i = 0; i<arr.size()-1;++i){

            for(int j = i+1; j<arr.size();++j){

                if(arr.get(i).equals(arr.get(j))){

                    System.out.println(arr.get(j));
                    arr.remove(j);


                }

            }

        }

    }

}
