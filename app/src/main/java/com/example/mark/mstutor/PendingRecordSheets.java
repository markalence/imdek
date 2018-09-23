package com.example.mark.mstutor;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;


public class PendingRecordSheets extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<HashMap<String,Object>> recordSheetItems = new ArrayList<>();
    PendingRecordSheetAdapter rsa;
    private String PENDING_RECORD_SHEETS = "pendingrecordsheets";

    public PendingRecordSheets() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getData(){

        firestore.collection(PENDING_RECORD_SHEETS).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        recordSheetItems.add((HashMap<String,Object>)doc.getData());
                    }
                    rsa.notifyDataSetChanged();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rsa = new PendingRecordSheetAdapter(getContext(),recordSheetItems,this.getLayoutInflater());
        recyclerView.setAdapter(rsa);
        getData();
        return RootView;
    }
}
