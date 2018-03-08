package com.example.willbrom.chatme.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.willbrom.chatme.R;
import com.example.willbrom.chatme.model.Message;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class MessageAdapter extends FirestoreAdapter<MessageAdapter.ViewHolder> {

    public MessageAdapter(Query query) {
        super(query);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapShot(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageView;

        public ViewHolder(View itemView) {
            super(itemView);

            messageView = (TextView) itemView.findViewById(R.id.message_item_msg);
        }

        public void bind(final DocumentSnapshot snapshot) {
            Message message = snapshot.toObject(Message.class);
            messageView.setText(message.getMessage());
        }
    }
}
