package com.example.activetechproject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class DisplayActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, dateTextView, organizationTextView, timeTextView;
    private ImageView eventImageView;
    private Button interestedButton, locationButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String eventName;
    private Double latitude;
    private Double longitude;
    private boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeViews();
        String eventId = getIntent().getStringExtra("EVENT_ID");
        fetchEventData(eventId);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        dateTextView = findViewById(R.id.eventDate);
        organizationTextView = findViewById(R.id.eventOrganization);
        timeTextView = findViewById(R.id.eventTime);
        eventImageView = findViewById(R.id.eventImageView);
        interestedButton = findViewById(R.id.interested_button);
        locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> openLocationInGoogleMaps());
        interestedButton.setOnClickListener(v -> sendInterestEmail());
    }

    private void fetchEventData(String eventId) {
        DocumentReference docRef = db.collection("Events").document(eventId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                eventName = documentSnapshot.getString("Name");
                titleTextView.setText(eventName);
                descriptionTextView.setText(documentSnapshot.getString("Description"));
                organizationTextView.setText(documentSnapshot.getString("The organization"));
                dateTextView.setText(documentSnapshot.getString("Date"));
                timeTextView.setText(documentSnapshot.getString("Time"));
                String imageUrl = documentSnapshot.getString("Logo");


                Glide.with(DisplayActivity.this)
                        .load(imageUrl)
                        .into(eventImageView);


                Map<String, Object> locationData = (Map<String, Object>) documentSnapshot.get("Location");
                if (locationData != null) {
                    Boolean isOnlineValue = (Boolean) locationData.get("isOnline");
                    latitude = locationData.get("latitude") != null ? (Double) locationData.get("latitude") : null;
                    longitude = locationData.get("longitude") != null ? (Double) locationData.get("longitude") : null;
                    isOnline = isOnlineValue != null && isOnlineValue;


                    if (!isOnline && latitude != null && longitude != null) {
                        locationButton.setVisibility(View.VISIBLE);
                    } else {
                        locationButton.setVisibility(View.GONE);
                    }
                } else {
                    isOnline = true;
                    locationButton.setVisibility(View.GONE);
                }}else {
                Toast.makeText(DisplayActivity.this, "No data available", Toast.LENGTH_SHORT).show();
            }}).addOnFailureListener(e -> {
            Toast.makeText(DisplayActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            Log.e("DisplayActivity", "Error fetching data", e);});
    }

    private void sendInterestEmail() {
        String recipientEmail = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        if (recipientEmail == null) {
            Toast.makeText(this, "No user is logged in. Cannot send email.", Toast.LENGTH_SHORT).show();
            return;}

        String subject = "Confirmation of Interest in " + eventName;
        String message = "Thank you for showing interest in " + eventName +
                "! We encourage you to register for this event within the allowed duration. Best wishes!";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
            Toast.makeText(this, "Opening email app to send confirmation.", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found. Please install an email app.", Toast.LENGTH_LONG).show();}}

    private void openLocationInGoogleMaps() {
        if (!isOnline) {
            if (latitude != null && longitude != null) {
                String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Location data is not available.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "This event is online; no physical location available.", Toast.LENGTH_SHORT).show();}}}