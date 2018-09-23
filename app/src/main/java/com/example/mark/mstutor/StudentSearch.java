package com.example.mark.mstutor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentSearch extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<HashMap<String,Object>> searchResults = new ArrayList<>();
    SearchAdapter searchAdapter;
    public static HashMap<String,Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView studentName = findViewById(R.id.searchStudentName);
        setRecyclerView();

        searchView = findViewById(R.id.studentSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String inputQuery) {

                String query = inputQuery.toLowerCase();
                searchResults.clear();
                searchAdapter.notifyDataSetChanged();

                Toast.makeText(getBaseContext(), StringUtils.capitalize(query), Toast.LENGTH_SHORT).show();
                Query firstNameQuery;
                firstNameQuery = firestore.collection("students")
                        .whereEqualTo("firstName", StringUtils.capitalize(query));

                Query lastNameQuery;
                lastNameQuery = firestore.collection("students")
                        .whereEqualTo("lastName",StringUtils.capitalize(query));

                firstNameQuery.get(Source.SERVER)
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()){
                                    for(DocumentSnapshot doc : task.getResult()){
                                        HashMap<String,Object> userData = (HashMap<String, Object>)doc.getData();
                                        userData.put("username",doc.getId());
                                        searchResults.add(userData);
                                        searchAdapter.notifyItemInserted(searchResults.size()-1);
                                    }
                                }
                            }
                        });

                lastNameQuery.get(Source.SERVER)
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()){
                                    for(DocumentSnapshot doc : task.getResult()){
                                        HashMap<String,Object> userData = (HashMap<String, Object>)doc.getData();
                                        userData.put("username",doc.getId());
                                        searchResults.add(userData);
                                        searchAdapter.notifyItemInserted(searchResults.size()-1);
                                    }

                                }
                            }
                        });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void setRecyclerView() {
        recyclerView = findViewById(R.id.studentSheet);
        searchAdapter = new SearchAdapter(getBaseContext(),searchResults);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
