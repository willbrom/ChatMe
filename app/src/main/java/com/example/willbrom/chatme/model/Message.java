package com.example.willbrom.chatme.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Message POJO.
 */
@IgnoreExtraProperties
public class Message {

    private String message;

    public Message() {}

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
