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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupAdapter extends RecyclerView.Adapter<SignupAdapter.ViewHolder> {


    Context mContext;
    ArrayList<HashMap<String, String>> mDataset;
    ArrayList<HashMap<String, String>> details;

    public SignupAdapter(Context context, ArrayList<HashMap<String, String>> dataset) {
        mContext = context;
        mDataset = dataset;
        details = new ArrayList<>();
    }

    public void addDay(HashMap<String, String> dataset) {
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

        String day = mDataset.get(position).get("day");
        String hours = mDataset.get(position).get("hours");
        String time = mDataset.get(position).get("time");

        holder.dayInformation
                .setText("Day: " + day + "\n" + "Time: " + time + "\n" + "Hours: " + hours);

        holder.dayRemove.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayInformation;
        ImageButton dayRemove;

        public ViewHolder(View v) {
            super(v);
            dayInformation = v.findViewById(R.id.dayInfo);
            dayRemove = v.findViewById(R.id.dayRemove);
        }

    }


}
