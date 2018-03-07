package com.example.willbrom.chatme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String DOCUMENT_SNAP_SHOT_ADDED_WITH_ID = "DocumentSnapShot added with ID: ";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ERROR_ADDING_DOCUMENT = "Error adding document: ";

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
    }

    public void onClickFireStore(View view) {
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Will");
        user.put("last", "Brom");
        user.put("born", 1911);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, DOCUMENT_SNAP_SHOT_ADDED_WITH_ID + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, ERROR_ADDING_DOCUMENT + e);
                    }
                });
    }
}
