package com.example.mark.mstutor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentDetails extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText grade;
    EditText school;
    Button setDays;

    public String fName;
    public String lName;
    public String mGrade;
    public String mSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        firstName = (EditText)findViewById(R.id.signupFirstName);
        lastName = (EditText)findViewById(R.id.signupLastName);
        grade = (EditText)findViewById(R.id.signupGrade);
        school = (EditText)findViewById(R.id.signupSchool);
        setDays = (Button)findViewById(R.id.signupSetDays);

        fName = firstName.getText().toString();
        lName = lastName.getText().toString();
        mGrade = grade.getText().toString();
        mSchool = school.getText().toString();

        setDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDetails.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
