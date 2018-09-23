package com.example.mark.mstutor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    ArrayList<HashMap<String,Object>> mDataset;
    Context mContext;

    SearchAdapter(Context context, ArrayList<HashMap<String,Object>> dataset){
        mContext = context;
        mDataset = dataset;
    }


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        SearchAdapter.ViewHolder vh = new SearchAdapter.ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ViewHolder holder, final int position) {
        String school = mDataset.get(position).get("school").toString();
        String firstName = mDataset.get(position).get("firstName").toString();
        String lastName = mDataset.get(position).get("lastName").toString();

        holder.searchDetails.setText(firstName + " " + lastName + "\n" + school);
        holder.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentSearch.userData = mDataset.get(position);
                Intent intent = new Intent(mContext,StudentInfo.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

    Button searchButton;
    TextView searchDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            searchButton = itemView.findViewById(R.id.searchButton);
            searchDetails = itemView.findViewById(R.id.searchDetails);
        }
    }
}
