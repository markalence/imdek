package com.example.mark.mstutor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, Object>> mDataset;

    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String DATE = "date";
    private String DATE_FORMAT = "HH:mm";
    private String HOURS = "hours";

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView details;

        public ViewHolder (View v){

            super(v);

            name = v.findViewById(R.id.scheduleName);
            details = v.findViewById(R.id.scheduleDetails);

        }

    }

    public ScheduleAdapter(Context context, ArrayList<HashMap<String, Object>> dataset){

        mContext = context;
        mDataset = dataset;

    }

    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = mDataset.get(position).get(FIRST_NAME).toString() + " " + mDataset.get(position).get(LAST_NAME).toString();
        Timestamp today = (Timestamp) mDataset.get(position).get(DATE);
        Date date = (today.toDate());
        SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);
        String details = sfd.format(date) + " for " + mDataset.get(position).get(HOURS) + " hours";


        holder.name.setText(name);
        holder.details.setText(details);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
