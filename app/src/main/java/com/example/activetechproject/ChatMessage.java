package com.example.activetechproject;

import android.net.Uri;

public class ChatMessage {
    private String username;
    private String message;
    private Uri imageUri;

    public ChatMessage(String username, String message, Uri imageUri) {
        this.username = username;
        this.message = message;
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
