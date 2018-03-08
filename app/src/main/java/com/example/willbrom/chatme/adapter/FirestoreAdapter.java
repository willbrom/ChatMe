package com.example.willbrom.chatme.adapter;

import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements EventListener<QuerySnapshot> {

    private static final String TAG = "Firestore Adapter";

    private Query mQuery;
    private ListenerRegistration mRegistration;

    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

        if (e != null) {
            return;
        }

        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            DocumentSnapshot snapshot = change.getDocument();

            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
            }
        }

        onDataChange();
    }

    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    protected DocumentSnapshot getSnapShot(int index) {
        return mSnapshots.get(index);
    }

    protected void onDataChange() {}

    protected void onError(FirebaseFirestoreException e) {}
}
