package com.example.mark.mstutor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentDetails extends AppCompatActivity {

    private EditText fName;
    private EditText lName;
    private EditText mPassword;
    private Button setSchool;
    private FloatingActionButton moveForward;
    private EditText mUsername;
    private Spinner gradeSpinner;
    private FirebaseFirestore firestore;
    public static HashMap<String,Object> studentData;

    public String firstName;
    public String lastName;
    public String grade;
    public static String school;
    public String username;
    public String password;
    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String GRADE = "grade";
    private String SCHOOL = "school";
    private String PASSWORD = "password";
    private String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        studentData = new HashMap<>();
        final ArrayList<String> schoolList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("schools")
                .orderBy("schoolName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                schoolList.add((String)doc.get("schoolName"));
                            }
                            grunge(schoolList);
                        }
                    }
                });

        fName = findViewById(R.id.signupFirstName);
        lName = findViewById(R.id.signupLastName);
        mUsername = findViewById(R.id.signupUsername);
        mPassword = findViewById(R.id.signupPassword);
        moveForward = findViewById(R.id.signup1Forward);
        setSchool = findViewById(R.id.setSchool);
    }


    public void grunge(final ArrayList<String> schoolList) {

        final AlertDialog.Builder schoolDialogBuilder;
        schoolDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(StudentDetails.this, R.style.CustomDialogTheme));
        schoolDialogBuilder.setTitle("Choose a school");
        schoolDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                schoolDialogBuilder.create().dismiss();
                setSchool.setText(school);
            }
        });

        schoolDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                schoolDialogBuilder.create().dismiss();
            }
        });
        CharSequence[] schoolArray = new CharSequence[schoolList.size()];
        schoolArray = schoolList.toArray(schoolArray);

        schoolDialogBuilder.setSingleChoiceItems(schoolArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                school = (String) schoolList.get(which);
                Toast.makeText(getBaseContext(), school, Toast.LENGTH_SHORT).show();
            }
        });

        setSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolDialogBuilder.create().show();
            }
        });


        moveForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = StringUtils.capitalize(fName.getText().toString().toLowerCase());
                lastName = StringUtils.capitalize(lName.getText().toString().toLowerCase());
                username = mUsername.getText().toString().toLowerCase();
                password = mPassword.getText().toString();

                boolean fieldsAreEmpty = firstName.isEmpty()
                        || lastName.isEmpty()
                        || username.isEmpty()
                        || grade.isEmpty()
                        || password.isEmpty();

                if(!fieldsAreEmpty) {

                    studentData.put(USERNAME, username);
                    studentData.put(FIRST_NAME, firstName);
                    studentData.put(LAST_NAME, lastName);
                    studentData.put(GRADE, grade);
                    studentData.put(SCHOOL, school);
                    studentData.put(PASSWORD, password);
                    Intent intent = new Intent(StudentDetails.this, SignupActivity2.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(getBaseContext(),"Please fill in all fields", Toast.LENGTH_SHORT).show();}
            }
        });

        gradeSpinner = findViewById(R.id.gradeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.grade_array,android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grade = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
