package com.example.activetechproject;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DisplayActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, dateTextView, organizationTextView, timeTextView;
    private ImageView eventImageView;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display); // Connect to XML

        // Bind elements to the interface
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        dateTextView = findViewById(R.id.eventDate);
        organizationTextView = findViewById(R.id.eventOrganization);
        timeTextView = findViewById(R.id.eventTime);
        eventImageView = findViewById(R.id.eventImageView);

        // Event_Id fetched from previous page
        String eventId = getIntent().getStringExtra("EVENT_ID");

        // Connect to Firestore
        db = FirebaseFirestore.getInstance();

        // Event details brought from Firestore
        DocumentReference docRef = db.collection("Events").document(eventId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                titleTextView.setText(documentSnapshot.getString("Name"));
                descriptionTextView.setText(documentSnapshot.getString("Description"));
                //organizationTextView.setText(documentSnapshot.getString("The organization"));
                locationTextView.setText(documentSnapshot.getString("Location"));
                dateTextView.setText(documentSnapshot.getString("Date"));
                timeTextView.setText(documentSnapshot.getString("Time"));
                String imageUrl = documentSnapshot.getString("Logo");
                Glide.with(DisplayActivity.this)
                        .load(imageUrl)
                        .into(eventImageView);
            }
        });

        // Button to go back to the previous activity
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed()); // Go back to the previous activity
    }
}
