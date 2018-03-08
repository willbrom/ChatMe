package com.example.willbrom.chatme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.willbrom.chatme.adapter.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String DOCUMENT_ADDED_WITH_ID = "DocumentSnapShot added with ID: ";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ERROR_ADDING_DOCUMENT = "Error adding document: ";
    public static final String MESSAGES = "messages";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";

    private EditText mMessageEditText;
    private RecyclerView mMessageRecyclerView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageEditText = (EditText)  findViewById(R.id.editText_message);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_message);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        initFirestore();
        initRecyclerView();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection(MESSAGES)
            .orderBy(TIMESTAMP);
    }

    private void initRecyclerView() {
        if (mQuery == null) {
        }

        mAdapter = new MessageAdapter(mQuery) {
            @Override
            protected void onDataChange() {

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);

        mMessageRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    public void onClickFireStore(View view) {
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Will");
        user.put("last", "Brom");
        user.put("born", 1911);

        mFirestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, DOCUMENT_ADDED_WITH_ID + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, ERROR_ADDING_DOCUMENT + e);
                    }
                });
    }

    public void sendMessage(View view) {
        String text = mMessageEditText.getText().toString();
        Date currentTime = Calendar.getInstance().getTime();

        if (!TextUtils.isEmpty(text)) {
            Map<String, Object> message = new HashMap<>();
            message.put(MESSAGE, text);
            message.put(TIMESTAMP, currentTime);

            mFirestore.collection(MESSAGES)
                    .add(message)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, DOCUMENT_ADDED_WITH_ID + task.getResult().getId());
                            } else {
                                Log.d(TAG, ERROR_ADDING_DOCUMENT);
                            }
                        }
                    });
        }
    }
}
