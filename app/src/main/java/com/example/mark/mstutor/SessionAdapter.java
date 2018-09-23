package com.example.mark.mstutor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    ArrayList<HashMap<String, Object>> mDataset;
    Context mContext;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private String DATE_FORMAT = "EEEE, MMMM dd";
    private String HOUR_FORMAT = "kk:mm";
    private String HOURS = "hours";
    private String DATE = "date";
    SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);


    public SessionAdapter(Context context, ArrayList<HashMap<String, Object>> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_cardview, parent, false);
        SessionAdapter.ViewHolder vh = new SessionAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.ViewHolder holder, final int position) {


        //collection references for in case a session is deleted
        SimpleDateFormat hourFormat = new SimpleDateFormat(HOUR_FORMAT);
        final Timestamp timestamp = (Timestamp) mDataset.get(position).get(DATE);
        final Date dateRecord = timestamp.toDate();
        String dateString = sfd.format(dateRecord) + " at " + hourFormat.format(dateRecord);
        String hourString = mDataset.get(position).get(HOURS).toString() + " hours";
        holder.sessionInfo.setText(dateString + "\n" + hourString);
        holder.documentId = (String) mDataset.get(position).get("docId");

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sessionInfo;
        String documentId;
        ConstraintLayout cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            sessionInfo = itemView.findViewById(R.id.sessionInfo);
            cardView = itemView.findViewById(R.id.cardLayout);
        }
    }
}