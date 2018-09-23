package com.example.mark.mstutor;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

public class SessionSwipeController extends ItemTouchHelper.Callback {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    boolean clickable;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        clickable = true;
        final SessionAdapter sessionAdapter = (SessionAdapter) SearchSchedule.recyclerView.getAdapter();
        final int copyPosition = viewHolder.getAdapterPosition();
        final HashMap<String, Object> copyMap = sessionAdapter.mDataset.get(copyPosition);

        final Snackbar snackbar = Snackbar
                .make(StudentInfo.sessionView, "Session canceled.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable) {
                    clickable = false;
                    copyMap.remove("docId");
                    firestore.collection("schedule")
                            .add(copyMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                copyMap.put("docId", task.getResult().getId());
                                sessionAdapter.mDataset.add(copyPosition, copyMap);
                                sessionAdapter.notifyItemInserted(copyPosition);
                            } else {
                                Toast.makeText(sessionAdapter.mContext, "Couldn't re-add session at this time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        snackbar.setActionTextColor(Color.rgb(25, 172, 172));

        if (viewHolder.getAdapterPosition() != -1) {
            sessionAdapter.mDataset.remove(copyPosition);
            sessionAdapter.notifyItemRemoved(copyPosition);
            firestore.collection("schedule")
                    .document((String) copyMap.get("docId"))
                    .delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sessionAdapter.mDataset.add(copyPosition, copyMap);
                    sessionAdapter.notifyItemInserted(copyPosition);
                    Toast.makeText(sessionAdapter.mContext, "Could not cancel session, try again later.", Toast.LENGTH_SHORT).show();
                }
            });
            snackbar.show();
        }
    }
}
