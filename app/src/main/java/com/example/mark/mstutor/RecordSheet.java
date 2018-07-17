package com.example.mark.mstutor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class RecordSheet extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    private ArrayList<HashMap<String,Object>> recordSheetItems = new ArrayList<>();

    private String HOURS = "hours";
    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String MODULE = "module";
    private String DATE = "date";
    private String USERNAME = "username";
    private String PENDING_RECORD_SHEETS = "pendingrecordsheets";



    public RecordSheet() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    public void setRecyclerView(){


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecordSheetAdapter rsa = new RecordSheetAdapter(getContext(),recordSheetItems,this.getLayoutInflater());
        recyclerView.setAdapter(rsa);


    }


    public void getData(){

        db.collection(PENDING_RECORD_SHEETS).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){


                    for(DocumentSnapshot doc : task.getResult()){

                        HashMap<String,Object> map = new HashMap<>();
                        map.put(FIRST_NAME,doc.get(FIRST_NAME).toString());
                        System.out.println(doc.get(FIRST_NAME).toString());
                        map.put(LAST_NAME,doc.get(LAST_NAME).toString());
                        Timestamp timestamp = doc.getTimestamp(DATE);
                        map.put(DATE,timestamp);
                        map.put(USERNAME,doc.get(USERNAME).toString());
                        map.put(HOURS,doc.get(HOURS).toString());
                        map.put(MODULE,doc.get(MODULE).toString());
                        System.out.println(map);
                        recordSheetItems.add(map);
                    }

                    setRecyclerView();

                }

            }
        }) ;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View RootView = inflater.inflate(R.layout.fragment_record_sheet, container, false);
        recyclerView = RootView.findViewById(R.id.recordSheet);
        getData();
        return RootView;
    }


}
