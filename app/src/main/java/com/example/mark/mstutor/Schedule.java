package com.example.mark.mstutor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class Schedule extends Fragment {

    FirebaseFirestore firestore;
    ArrayList<HashMap<String, Object>> scheduleItems = new ArrayList<>();
    RecyclerView recyclerView;

    private String SCHEDULE = "schedule";
    private String DATE = "date";
    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String HOURS = "hours";


    public Schedule() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();

    }

    public void getData() {

        Calendar dateToday = new GregorianCalendar();
        dateToday.set(Calendar.HOUR_OF_DAY, 0);
        dateToday.set(Calendar.MINUTE, 0);
        dateToday.set(Calendar.SECOND, 0);
        dateToday.set(Calendar.MILLISECOND, 0);

        Calendar dateTomorrow = new GregorianCalendar();
        dateTomorrow.set(Calendar.HOUR_OF_DAY, 0);
        dateTomorrow.set(Calendar.MINUTE, 0);
        dateTomorrow.set(Calendar.SECOND, 0);
        dateTomorrow.set(Calendar.MILLISECOND, 0);
        dateTomorrow.add(Calendar.DAY_OF_MONTH, 1);


        Date today = dateToday.getTime();
        Date tomorrow = dateTomorrow.getTime();

        Timestamp timestampToday = new Timestamp(today);
        Timestamp timestampTomorrow = new Timestamp(tomorrow);

        Query query = firestore.collection(SCHEDULE).whereGreaterThan(DATE, timestampToday)
                .whereLessThan(DATE, timestampTomorrow).orderBy(DATE, Query.Direction.ASCENDING);

        query.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (DocumentSnapshot doc : task.getResult()) {

                        HashMap<String, Object> map = new HashMap<>();

                        map.put(FIRST_NAME, doc.get(FIRST_NAME));
                        map.put(LAST_NAME, doc.get(LAST_NAME));
                        map.put(HOURS, doc.get(HOURS));
                        Timestamp timestamp = doc.getTimestamp(DATE);
                        map.put(DATE, timestamp);

                        scheduleItems.add(map);

                    }

                    setRecyclerView();

                }
            }
        });


    }

    public void setRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ScheduleAdapter sa = new ScheduleAdapter(getContext(), scheduleItems);
        recyclerView.setAdapter(sa);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = RootView.findViewById(R.id.scheduleRecyclerView);
        getData();
        return RootView;
    }


}
