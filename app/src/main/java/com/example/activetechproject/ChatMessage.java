package com.example.activetechproject;

import android.net.Uri;

public class ChatMessage {
    private String senderId;
    private String message;
    private String timesTamp;
    private Uri imageUri;

    public ChatMessage() {



    }

    public ChatMessage(String senderId, String message, Uri imageUri) {
        this.senderId = senderId;
        this.message = message;
        //this.timesTamp = timesTamp;
        this.imageUri= imageUri;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

//    public String getTimesTamp() {
//        return timesTamp;
//    }

    public Uri getImageUri() {
        return imageUri;
    }

}