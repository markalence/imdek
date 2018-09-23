package com.example.mark.mstutor;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.oob.SignUp;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity2 extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FloatingActionButton moveForward;
    public static ArrayList<HashMap<String,String>> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        recyclerView = findViewById(R.id.contactRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        pageInit();




        moveForward = findViewById(R.id.signup2MoveForward);

        moveForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity2.this, SignupActivity.class);
                startActivity(intent);
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

    public void pageInit(){

        contacts = new ArrayList<>();
        final ContactAdapter contactAdapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(contactAdapter);
        View v = getLayoutInflater().inflate(R.layout.contact_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity2.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        final AlertDialog contactDialog = builder.create();
        contactDialog.setView(v);

        final EditText contactDetails = v.findViewById(R.id.contactDetails);
        final EditText contactDescription = v.findViewById(R.id.contactDescription);
        Button contactConfirm = v.findViewById(R.id.contactConfirm);
        Button contactCancel = v.findViewById(R.id.contactCancel);
        Button addContact = findViewById(R.id.addContact);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.show();
            }
        });

        contactConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactDet = contactDetails.getText().toString();
                String contactDes = contactDescription.getText().toString();
                HashMap<String,String> map = new HashMap<>();
                map.put(contactDes,contactDet);
                contacts.add(map);
                contactAdapter.notifyItemInserted(contacts.size());
                contactDialog.dismiss();
            }
        });

        contactCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
            }
        });


    }

}
