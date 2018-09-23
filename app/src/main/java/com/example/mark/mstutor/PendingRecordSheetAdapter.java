package com.example.mark.mstutor;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class PendingRecordSheetAdapter extends RecyclerView.Adapter<PendingRecordSheetAdapter.ViewHolder> {


    private ArrayList<HashMap<String, Object>> mDataset;
    private Context mContext;
    private LayoutInflater mInflater;
    private String HOURS = "hours";
    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String MODULE = "module";
    private String DATE = "date";
    private String USERNAME = "username";
    private String RECORD_SHEETS = "recordsheets";
    private String PENDING_RECORD_SHEETS = "pendingrecordsheets";
    private String COMMENT = "comment";
    private String DATE_FORMAT = "dd MMMM";
    private String date;
    private String hours;
    private String module;
    private String firstName;
    private String lastName;

    AlertDialog.Builder builder;
    View dialogView;
    AlertDialog commentDialog;
    Button cancelButton;
    Button commentConfirm;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateRecord;
        Timestamp dateObject;
        TextView moduleRecord;
        TextView nameRecord;
        ImageButton addComment;
        String username;
        EditText editText;

        public ViewHolder(View v) {

            super(v);
            dateRecord = v.findViewById(R.id.dateRecord);
            moduleRecord = v.findViewById(R.id.moduleRecord);
            nameRecord = v.findViewById(R.id.nameRecord);
            addComment = v.findViewById(R.id.addComment);
            username = new String();
            editText = dialogView.findViewById(R.id.commentText);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PendingRecordSheetAdapter(Context context, ArrayList<HashMap<String, Object>> myDataset, LayoutInflater inflater) {
        mDataset = myDataset;
        mContext = context;
        mInflater = inflater;

        builder = new AlertDialog.Builder(mContext);
        dialogView = mInflater.inflate(R.layout.add_comment, null);
        builder.setView(dialogView);
        commentDialog = builder.create();
        cancelButton = dialogView.findViewById(R.id.commentCancel);
        commentConfirm = dialogView.findViewById(R.id.commentConfirm);
        commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialog.dismiss();
            }
        });

    }


    // Create new views (invoked by the layout manager)
    @Override
    public PendingRecordSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_sheet_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        System.out.println(position);
        SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);
        Timestamp ts = (Timestamp) mDataset.get(holder.getAdapterPosition()).get(DATE);
        Date dateRecord = ts.toDate();
        date = sfd.format(dateRecord);
        holder.username = (String) mDataset.get(holder.getAdapterPosition()).get(USERNAME);

        hours = (String) mDataset.get(position).get(HOURS);
        if(!hours.equals("1")){
            holder.dateRecord.setText("Date: " + date + " for " + hours + " hours");
        }
        else{holder.dateRecord.setText("Date: " + date + " for " + hours + " hour");}
        module = (String) mDataset.get(holder.getAdapterPosition()).get(MODULE);
        firstName = (String) mDataset.get(holder.getAdapterPosition()).get(FIRST_NAME);
        lastName = (String) mDataset.get(position).get(LAST_NAME);
        holder.dateObject = (Timestamp) mDataset.get(holder.getAdapterPosition()).get(DATE);

        System.out.println(mDataset.get(holder.getAdapterPosition()) + "  " + position);

        holder.moduleRecord.setText("Module: "  + module);
        holder.nameRecord.setText(firstName + " " + lastName);
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentDialog.show();

                commentConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = holder.editText.getText().toString();
                        final HashMap<String, Object> map = mDataset.get(holder.getAdapterPosition());
                        map.put(COMMENT,comment);


                        firestore.collection(RECORD_SHEETS).add(map)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            updateDatabase(holder.getAdapterPosition(),holder);
                                        }
                                        else{
                                        Toast.makeText(mContext,"Check connection.", Toast.LENGTH_SHORT);}
                                    }
                                });
                        commentDialog.dismiss();
                    }
                });
            }
        });

    }












































    public void updateDatabase(final int position,final ViewHolder holder) {

        final HashMap<String, Object> copy = mDataset.get(holder.getAdapterPosition());
        mDataset.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
        System.out.println("POSTITION: " + holder.getAdapterPosition());


        for(HashMap<String,Object> map : mDataset){
            for(String key : map.keySet()){
                System.out.print(map.get(key) + " ");
            }
            System.out.print(mDataset.indexOf(map) + "\n");
        }

        firestore.collection(PENDING_RECORD_SHEETS)
                .whereEqualTo(DATE, holder.dateObject)
                .whereEqualTo(USERNAME, holder.username)
                .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        firestore.collection(PENDING_RECORD_SHEETS)
                                .document(doc.getId())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContext, "Comment added successfully", Toast.LENGTH_SHORT).show();

                                        }
                                        else{mDataset.add(holder.getAdapterPosition(),copy);
                                            notifyItemInserted(holder.getAdapterPosition());}
                                    }
                                });
                    }
                }
                else {
                    Toast.makeText(mContext, "Could not add comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}



