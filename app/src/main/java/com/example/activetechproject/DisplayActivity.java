package com.example.activetechproject;
import android.content.Intent;
import android.os.Bundle;
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

public class DisplayActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, dateTextView, organizationTextView, timeTextView;
    private ImageView eventImageView;
    private Button interestedButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display); // Connect to XML
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind elements to the interface
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        dateTextView = findViewById(R.id.eventDate);
        organizationTextView = findViewById(R.id.eventOrganization);
        timeTextView = findViewById(R.id.eventTime);
        eventImageView = findViewById(R.id.eventImageView);
        interestedButton = findViewById(R.id.interested_button);
        // Event_Id fetched from previous page
        String eventId = getIntent().getStringExtra("EVENT_ID");

        // Connect to Firestore
        db = FirebaseFirestore.getInstance();

        // Event details brought from Firestore
        DocumentReference docRef = db.collection("Events").document(eventId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                eventName = documentSnapshot.getString("Name");
                titleTextView.setText(eventName);
                //titleTextView.setText(documentSnapshot.getString("Name"));
                descriptionTextView.setText(documentSnapshot.getString("Description"));
                organizationTextView.setText(documentSnapshot.getString("The organization"));
                locationTextView.setText(documentSnapshot.getString("Location"));
                dateTextView.setText(documentSnapshot.getString("Date"));
                timeTextView.setText(documentSnapshot.getString("Time"));
                String imageUrl = documentSnapshot.getString("Logo");
                Glide.with(DisplayActivity.this)
                        .load(imageUrl)
                        .into(eventImageView);
            }
        });
        // Handle "Interested in the event" button click to send an email
        Button interestedButton = findViewById(R.id.interested_button);
        interestedButton.setOnClickListener(v -> sendInterestEmail());
        // Button to go back to the previous activity
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed()); // Go back to the previous activity
    }

    // Method to send email intent
    private void sendInterestEmail() {
        // Get the current user
        String recipientEmail = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (recipientEmail == null) {
            Toast.makeText(this, "No user is logged in. Cannot send email.", Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = "Confirmation of Interest in " + eventName;
        String message = "Thank you for showing interest in " + eventName +
                "! We encourage you to register for this event within the allowed duration. Best wishes!";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822"); // MIME type for email
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
            Toast.makeText(this, "Opening email app to send confirmation.", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found. Please install an email app.", Toast.LENGTH_LONG).show();
        }
    }}
