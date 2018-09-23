package com.example.mark.mstutor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchSchedule extends Fragment {
    FirebaseFirestore firestore;
    SessionAdapter sessionAdapter;
    static RecyclerView recyclerView;
    static View sessionView;
    View rootView = null;
    SessionSwipeController swipeController = new SessionSwipeController();
    private String SCHEDULE = "schedule";
    private String USERNAME = "username";
    private String DATE = "date";
    private ArrayList<HashMap<String, Object>> sessionItems = new ArrayList<>();

    public SearchSchedule() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            rootView = inflater.inflate(R.layout.fragment_search_schedule, container, false);
            firestore = FirebaseFirestore.getInstance();
            getData();
            sessionView = rootView.findViewById(android.R.id.content);
            recyclerViewInit(rootView);
            return rootView;

    }

    public void getData() {

        Query upcomingSessions = firestore.collection(SCHEDULE)
                .whereEqualTo(USERNAME, StudentSearch.userData.get(USERNAME))
                .orderBy(DATE, Query.Direction.ASCENDING)
                .limit(10);

        upcomingSessions.get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (DocumentSnapshot doc : task.getResult()) {
                                HashMap<String, Object> map = (HashMap<String, Object>) doc.getData();
                                map.put("docId", doc.getId());
                                sessionItems.add(map);
                            }
                            sessionAdapter.notifyDataSetChanged();

                        } else {

                            Log.d("ERRORMAKEMESAD", task.getException().toString());
                            Toast.makeText(getContext(), "Error. Please tell your manager.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void recyclerViewInit(View rootView) {
        recyclerView = rootView.findViewById(R.id.searchScheduleRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionAdapter = new SessionAdapter(getContext(), sessionItems);
        recyclerView.setAdapter(sessionAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
