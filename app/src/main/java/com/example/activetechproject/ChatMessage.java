package com.example.activetechproject;

import android.net.Uri;

public class ChatMessage {
    private String senderId;
    private String message;
    private String timestamp;
    private Uri imageUri;

    public ChatMessage() {
        // مطلوب لقراءة البيانات من Firebase
    }

    public ChatMessage(String senderId, String message, String timesTemp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUri= imageUri;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}