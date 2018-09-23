package com.example.mark.mstutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<HashMap<String, String>> dayData = new ArrayList<>();
    HashMap<String, String> oneDay = new HashMap<>();
    ArrayList<HashMap<String, Object>> sessions = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private String HOURS = "hours";
    private String DAY = "day";
    private String TIME = "time";
    private String STUDENTS = "students";
    private String[] dayList = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] timeList = new String[]{"13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"};
    private String[] hourList = new String[]{"1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"};
    ArrayList<Integer> dayValues = new ArrayList<>();

    private AlertDialog.Builder alertDialogBuilder;

    private Button dayConfirm;
    private Button timeConfirm;
    private Button hourConfirm;
    private Button dayCancel;
    private Button timeCancel;
    private Button hourCancel;
    private Button addDay;
    private Button addStudent;
    private SignupAdapter signupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sessions.clear();
        signupAdapter = new SignupAdapter(SignupActivity.this, dayData);
        addDay = findViewById(R.id.addDay);
        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDayMap();
                initializePage(SignupActivity.this);
            }
        });

        addStudent = findViewById(R.id.addStudent);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentDetails.studentData.put("days", dayData);
                StudentDetails.studentData.put("contacts",SignupActivity2.contacts);
                System.out.println(StudentDetails.studentData);
                Toast.makeText(getBaseContext(), "Student added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);

                firestore.collection(STUDENTS)
                        .document(StudentDetails.studentData.get("username").toString())
                        .set(StudentDetails.studentData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            bookUpcomingSessions();

                        } else {
                            Toast.makeText(getBaseContext(), "Could not add student at this time. Check connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }

    public void initializePage(final Context context) {

        recyclerView = findViewById(R.id.setDaysRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(signupAdapter);

        View dayView = LayoutInflater.from(context).inflate(R.layout.wheel, null);
        final View timeView = LayoutInflater.from(context).inflate(R.layout.wheel, null);
        final View hourView = LayoutInflater.from(context).inflate(R.layout.wheel, null);

        dayConfirm = dayView.findViewById(R.id.wheelConfirm);
        dayCancel = dayView.findViewById(R.id.wheelCancel);
        timeConfirm = timeView.findViewById(R.id.wheelConfirm);
        timeCancel = timeView.findViewById(R.id.wheelCancel);
        hourConfirm = hourView.findViewById(R.id.wheelConfirm);
        hourCancel = hourView.findViewById(R.id.wheelCancel);

        alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(dayView);

        dayConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dayAlreadyChosen()) {
                    alertDialog.setContentView(timeView);
                    alertDialog.show();
                } else {
                    Toast.makeText(context, "You already chose that day.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        dayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDayMap();
                alertDialog.dismiss();
            }
        });

        timeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setContentView(hourView);
                alertDialog.show();
            }
        });

        timeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDayMap();
                alertDialog.dismiss();
            }
        });


        hourConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> dayInstance = oneDay;
                dayData.add(dayInstance);
                signupAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
                Log.d("SUBMITTING", dayData.toString());
            }
        });

        hourCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDayMap();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        NumberPicker dayPicker = dayView.findViewById(R.id.numberPicker);
        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(dayList.length - 1);
        dayPicker.setDisplayedValues(dayList);

        NumberPicker timePicker = timeView.findViewById(R.id.numberPicker);
        timePicker.setMinValue(0);
        timePicker.setMaxValue(timeList.length - 1);
        timePicker.setDisplayedValues(timeList);

        NumberPicker hourPicker = hourView.findViewById(R.id.numberPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(hourList.length - 1);
        hourPicker.setDisplayedValues(hourList);

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                oneDay.put(DAY, dayList[newVal]);
            }
        });

        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                oneDay.put(TIME, timeList[newVal]);
            }
        });

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                oneDay.put(HOURS, hourList[newVal]);
            }
        });


    }

    public void resetDayMap() {

        oneDay = new HashMap<>();
        oneDay.put(DAY, dayList[0]);
        oneDay.put(TIME, timeList[0]);
        oneDay.put(HOURS, hourList[0]);

    }

    public boolean dayAlreadyChosen() {

        for (int i = 0; i < dayData.size(); ++i) {
            if (dayData.get(i).containsValue(oneDay.get("day"))) {
                return true;
            }
        }
        return false;
    }

    public void bookUpcomingSessions() {

        for (HashMap<String, String> hashmap : dayData) {

            Calendar calendar = Calendar.getInstance();
            String day;
            String time[];
            String hours;

            day = hashmap.get("day");
            time = hashmap.get("time").split(":");
            hours = hashmap.get("hours");

            calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(day));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

            if (calendar.before(Calendar.getInstance().getTime())) {
                calendar.set(Calendar.WEEK_OF_YEAR, Calendar.WEEK_OF_YEAR + 1);
            }


            HashMap<String, Object> oneSession = new HashMap<>();
            oneSession.put("date", calendar.getTime());
            oneSession.put("hours", hours);
            sessions.add(oneSession);

        }

        ArrayList<HashMap<String, Object>> documentSessions = new ArrayList<>();

        for (int i = 0; i < sessions.size(); ++i) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) sessions.get(i).get("date"));
            calendar.set(Calendar.SECOND, 0);
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            for (int j = 1; j <= 4; ++j) {

                HashMap<String, Object> map = new HashMap<>();

                calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear + j);
                map.put("date", calendar.getTime());
                map.put("hours", sessions.get(i).get("hours"));
                map.put("firstName", StudentDetails.studentData.get("firstName").toString());
                map.put("lastName", StudentDetails.studentData.get("lastName").toString());
                map.put("username", StudentDetails.studentData.get("username").toString());
                documentSessions.add(map);

            }

        }

        for (HashMap<String, Object> map : documentSessions) {
            firestore.collection("schedule").add(map);
        }

    }

    public int getDayOfWeek(String day) {

        switch (day) {
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;

        }

        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
