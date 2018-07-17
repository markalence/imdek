package com.example.mark.mstutor;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordSheetAdapter extends RecyclerView.Adapter<RecordSheetAdapter.ViewHolder> {


    private ArrayList<HashMap<String, Object>> mDataset;
    private Context mContext;
    private LayoutInflater mInflater;
    private String HOURS = "hours";
    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String MODULE = "module";
    private String DATE = "date";
    private String USERNAME = "username";
    private String STUDENTS = "students";
    private String RECORD_SHEET = "recordsheet";
    private String PENDING_RECORD_SHEETS = "pendingrecordsheets";
    private String COMMENT = "comment";
    private String DATE_FORMAT = "dd/MM/yy";

    private String date;
    private Timestamp dateObject;
    private String hours;
    private String module;
    private String firstName;
    private String lastName;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateRecord;
        TextView hoursRecord;
        TextView moduleRecord;
        TextView nameRecord;
        Button addComment;


        public ViewHolder(View v) {

            super(v);

            dateRecord = v.findViewById(R.id.dateRecord);
            hoursRecord = v.findViewById(R.id.hoursRecord);
            moduleRecord = v.findViewById(R.id.moduleRecord);
            nameRecord = v.findViewById(R.id.nameRecord);
            addComment = v.findViewById(R.id.addComment);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordSheetAdapter(Context context, ArrayList<HashMap<String, Object>> myDataset, LayoutInflater inflater) {
        mDataset = myDataset;
        mContext = context;
        mInflater = inflater;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecordSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_sheet_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);
        Timestamp ts = (Timestamp) mDataset.get(position).get(DATE);
        System.out.println(mDataset.get(position));
        System.out.println(position);
        Date dateRecord = ts.toDate();
        date = sfd.format(dateRecord);


        hours = (String) mDataset.get(position).get(HOURS);
        module = (String) mDataset.get(position).get(MODULE);
        firstName = (String) mDataset.get(position).get(FIRST_NAME);
        lastName = (String) mDataset.get(position).get(LAST_NAME);
        dateObject = (Timestamp) mDataset.get(position).get(DATE);

        final String username = (String) mDataset.get(position).get(USERNAME);

        holder.dateRecord.setText("Date: " + date);
        holder.hoursRecord.setText("Hours: " + hours);
        holder.moduleRecord.setText("Module: " + module);
        holder.nameRecord.setText("Name: " + firstName + " " + lastName);
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dialogView = mInflater.inflate(R.layout.add_comment, null);
                builder.setView(dialogView);

                final AlertDialog dialog = builder.create();
                Button cancel = dialogView.findViewById(R.id.commentCancel);
                Button add = dialogView.findViewById(R.id.commentConfirm);
                final EditText text = dialogView.findViewById(R.id.commentText);
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = text.getText().toString();
                        Log.d("OUTPUT", comment);
                        final HashMap<String, Object> map = new HashMap<>();
                        map.put(MODULE, module);
                        map.put(DATE, dateObject);
                        map.put(HOURS, hours);
                        map.put(COMMENT, comment);
                        firestore.collection(STUDENTS).document(username).collection(RECORD_SHEET).add(map);

                        final CollectionReference collectionReference = firestore
                                .collection(PENDING_RECORD_SHEETS);

                        Query query = collectionReference
                                .whereEqualTo(MODULE, module)
                                .whereEqualTo(DATE, dateObject)
                                .whereEqualTo(USERNAME, username)
                                .whereEqualTo(HOURS, hours)
                                .whereEqualTo(FIRST_NAME, firstName)
                                .whereEqualTo(LAST_NAME, lastName);

                        query.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {


                                    for (DocumentSnapshot doc : task.getResult()) {

                                        collectionReference.document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                dialog.dismiss();
                                                Toast.makeText(mContext, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                                mDataset.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        });


                                    }


                                } else {
                                    Toast.makeText(mContext, "Could not add comment", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }
                        });


                    }
                });

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mDataset.size();
    }

}



