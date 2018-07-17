package com.example.mark.mstutor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupAdapter extends RecyclerView.Adapter<SignupAdapter.ViewHolder> {


    Context mContext;
    ArrayList<HashMap<String,Object>> mDataset = new ArrayList<>();
    ArrayList<HashMap<String,Object>> details;
    public SignupAdapter(Context context, ArrayList<HashMap<String,Object>> dataset) {
        mContext = context;
        mDataset = dataset;
        details =  new ArrayList<>();
    }

    public void addDay( HashMap<String,Object> dataset) {
        mDataset.add(dataset);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SignupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.signup_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.week_array, android.R.layout.simple_spinner_item);

        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.hour_array,android.R.layout.simple_spinner_item);

        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        holder.day.setAdapter(dayAdapter);
        holder.hours.setAdapter(hourAdapter);

        holder.day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,Object> map = new HashMap<>();
                map.put("day",parent.getItemAtPosition(position));
                details.add(map);
                Log.d("ADDING", "HERE I AM ADD DAY");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,Object> map = new HashMap<>();
                map.put("hours",parent.getItemAtPosition(position));
                details.add(map);
                Log.d("ADDING", "HERE I AM ADD DAY");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        HashMap<String,Object> map = new HashMap<>();
        map.put("time",holder.time.getText().toString());

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton cancel;
        EditText time;
        Spinner day;
        Spinner hours;

        public ViewHolder(View v){

            super(v);
            cancel = v.findViewById(R.id.dayCancel);
            hours = v.findViewById(R.id.hourSpinner);
            day = v.findViewById(R.id.daySpinner);
            time = v.findViewById(R.id.dayTime);

        }

    }





}
