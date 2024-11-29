package com.example.activetechproject;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {
    private static final String TAG = "ExploreActivity";
    private EditText etSearchEvent;
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_search) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });


        if (db == null) {
            Log.e(TAG, "Firestore initialization failed.");
            Toast.makeText(this, "Firestore not initialized!", Toast.LENGTH_SHORT).show();
        }

        Log.d("ExploreActivity", "Activity created");


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Binding UI components
        etSearchEvent = findViewById(R.id.et_search_event);
        recyclerViewEvents = findViewById(R.id.recycler_view_events);
        progressBar = findViewById(R.id.progress_bar);
        tvNoResults = findViewById(R.id.tv_no_results);

        // Setting up RecyclerView
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);

        // Add listener to search bar
        etSearchEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEvents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load events initially
        searchEvents("");

        // Back button

    }

    private void searchEvents(String searchText) {
        showLoading();
        Log.d(TAG, "Searching for: " + searchText);
        Query query = db.collection("Events")
                .orderBy("Name")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .limit(20);
        Toast.makeText(this, "Loading events, please wait...", Toast.LENGTH_SHORT).show();

        query.get().addOnCompleteListener(task -> {
            hideLoading();
            if (task.isSuccessful()) {
                eventList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    event.setId(document.getId());  // Add Document ID to Event
                    eventList.add(event);


                }
                eventAdapter.notifyDataSetChanged();

                if (eventList.isEmpty()) {
                    showNoResultsMessage();
                } else {
                    hideNoResultsMessage();
                }
            } else {
                Log.e(TAG, "Error fetching events", task.getException());
                Toast.makeText(ExploreActivity.this, "Error fetching events: " +
                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewEvents.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerViewEvents.setVisibility(View.VISIBLE);
    }

    private void showNoResultsMessage() {
        tvNoResults.setVisibility(View.VISIBLE);
        recyclerViewEvents.setVisibility(View.GONE);
    }

    private void hideNoResultsMessage() {
        tvNoResults.setVisibility(View.GONE);
        recyclerViewEvents.setVisibility(View.VISIBLE);
    }
}
