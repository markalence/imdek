package com.example.mark.mstutor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> mDataset;

    ContactAdapter(ArrayList<HashMap<String, String>> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(position);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText contactDescription;
        EditText contactDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            contactDescription = itemView.findViewById(R.id.contactDescription);
            contactDetails = itemView.findViewById(R.id.contactDetails);
        }

        public void setItem(int position) {

            String description = mDataset.get(position)
                    .keySet()
                    .toArray()[0]
                    .toString();

            String details = mDataset.get(position)
                    .get(description);

            contactDescription.setText(description);
            contactDetails.setText(details);

        }
    }
}
