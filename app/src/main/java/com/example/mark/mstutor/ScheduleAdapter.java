package com.example.mark.mstutor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, Object>> mDataset;
    ArrayList<Integer> selectedItems;

    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String DATE = "date";
    private String DATE_FORMAT = "HH:mm";
    private String HOURS = "hours";

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView details;
        ImageView imageView;
        String side;
        ImageButton toProfile;

        public ViewHolder (View v){
            super(v);
            imageView = v.findViewById(R.id.imageView);
            details = v.findViewById(R.id.scheduleDetails);
            toProfile = v.findViewById(R.id.toProfile);
            side = "front";
        }
    }

    public ScheduleAdapter(Context context, ArrayList<HashMap<String, Object>> dataset){
        mContext = context;
        mDataset = dataset;
        selectedItems = new ArrayList<>();
    }

    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String name = mDataset.get(position).get(FIRST_NAME).toString() + " " + mDataset.get(position).get(LAST_NAME).toString();
        Timestamp todayTimestamp =  (Timestamp) mDataset.get(position).get(DATE);
        Date today = todayTimestamp.toDate();
        SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);
        String details = sfd.format(today) + " for " + mDataset.get(position).get(HOURS) + " hour";
        if(!mDataset.get(position).get(HOURS).toString().equals("1")){details += "s";}

        String firstLetter = String.valueOf(mDataset.get(position).get(FIRST_NAME).toString().charAt(0));

        final TextDrawable drawableFront = TextDrawable.builder()
                .buildRound(firstLetter, ColorGenerator.MATERIAL.getRandomColor());



        holder.imageView.setImageDrawable(drawableFront);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.imageView, "scaleY",1f, 0f);
                oa1.setDuration(80);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.imageView, "scaleY", 0f, 1f);
                oa2.setDuration(80);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if(holder.side.equals("front")){
                        holder.imageView.setImageResource(R.drawable.ic_check_white_24dp);
                        holder.imageView.setBackgroundResource(R.drawable.circle);
                        holder.imageView.setPadding(30,30,30,30);
                        selectedItems.add(position);
                            holder.side="back";}
                        else{
                            holder.imageView.setImageDrawable(drawableFront);
                            holder.imageView.setBackgroundColor(Color.TRANSPARENT);
                            holder.imageView.setPadding(0,0,0,0);
                            holder.side = "front";
                            selectedItems.remove(selectedItems.indexOf(position));
                        }
                        oa2.start();
                    }
                });
                oa1.start();
            }
        });
        holder.details.setText(name + "\n" + details);

        holder.toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentSearch.userData = mDataset.get(position);
                Intent intent = new Intent(mContext,StudentInfo.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
