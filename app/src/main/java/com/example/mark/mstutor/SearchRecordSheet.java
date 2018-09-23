package com.example.mark.mstutor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class SearchRecordSheet extends Fragment {

    static TextView date;
    static TextView hours;
    static TextView module;
    static TextView comment;
    private String RECORD_SHEETS = "recordsheets";
    private String USERNAME = "username";
    private String DATE = "date";
    View rootView;
    private  HashMap<String,Object> userData;
    FirebaseFirestore firestore;
    RecordSheetAdapter rsa;
    private RecyclerView recyclerView;
    private ArrayList<HashMap<String, Object>> recordItems = new ArrayList<>();

    public SearchRecordSheet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            firestore = FirebaseFirestore.getInstance();
            rootView = inflater.inflate(R.layout.fragment_search_record_sheet, container, false);
            userData = StudentSearch.userData;
            date = rootView.findViewById(R.id.date);
            hours = rootView.findViewById(R.id.hours);
            module = rootView.findViewById(R.id.module);
            comment = rootView.findViewById(R.id.comment);
            getData();
            setRecyclerView(rootView);
            return rootView;

    }

    public void getData() {

        firestore.collection(RECORD_SHEETS)
                .whereEqualTo(USERNAME, userData.get(USERNAME))
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                recordItems.add((HashMap<String, Object>) doc.getData());
                            }
                            rsa.notifyDataSetChanged();
                        } else {
                            Log.d("BAD", task.getException().toString());
                            Toast.makeText(getContext(), "Couldn't connect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setRecyclerView(View rootView) {
        System.out.println(userData);
        recyclerView = rootView.findViewById(R.id.recordSheet);
        rsa = new RecordSheetAdapter(getContext(), recordItems);
        recyclerView.setAdapter(rsa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
