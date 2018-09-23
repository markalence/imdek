package com.example.mark.mstutor;

import android.content.Context;
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

public class RecordSheetAdapter extends RecyclerView.Adapter<RecordSheetAdapter.ViewHolder> {


    public ArrayList<HashMap<String, Object>> mDataset;
    public Context mContext;
    String dateFormat = "dd/MM/yy";
    SimpleDateFormat sfd = new SimpleDateFormat(dateFormat);

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateRecord;
        TextView hoursRecord;
        TextView moduleRecord;
        TextView commentRecord;


        public ViewHolder(View v) {
            super(v);
            dateRecord = v.findViewById(R.id.searchDateRecord);
            hoursRecord = v.findViewById(R.id.searchHoursRecord);
            moduleRecord = v.findViewById(R.id.searchModuleRecord);
            commentRecord = v.findViewById(R.id.searchCommentRecord);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordSheetAdapter(Context context, ArrayList<HashMap<String, Object>> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecordSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_sheet_search_item, parent, false);
        RecordSheetAdapter.ViewHolder vh = new RecordSheetAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecordSheetAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Timestamp timestamp = (Timestamp)mDataset.get(position).get("date");
        Date sessionDate = timestamp.toDate();

        holder.dateRecord.setText(sfd.format(sessionDate));
        holder.moduleRecord.setText((CharSequence) mDataset.get(position).get("module"));
        holder.hoursRecord.setText((CharSequence) mDataset.get(position).get("hours"));
        holder.commentRecord.setText((CharSequence) mDataset.get(position).get("comment"));

//        if(holder.commentRecord.getHeight() >= holder.moduleRecord.getHeight()){
//            //holder.moduleRecord.setHeight(holder.commentRecord.getHeight());
//            holder.dateRecord.setHeight(holder.commentRecord.getHeight());
//            holder.hoursRecord.setHeight(holder.commentRecord.getHeight());
//        }
//        else {
//            holder.commentRecord.setHeight(holder.moduleRecord.getHeight());
//            holder.dateRecord.setHeight(holder.moduleRecord.getHeight());
//            holder.hoursRecord.setHeight(holder.moduleRecord.getHeight());
//        }

        holder.dateRecord.setWidth(SearchRecordSheet.date.getWidth());
        holder.moduleRecord.setWidth(SearchRecordSheet.module.getWidth());
        holder.hoursRecord.setWidth(SearchRecordSheet.hours.getWidth());
        holder.commentRecord.setWidth(SearchRecordSheet.comment.getWidth());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}


