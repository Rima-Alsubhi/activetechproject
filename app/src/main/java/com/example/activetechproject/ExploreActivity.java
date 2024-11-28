package com.example.activetechproject;
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
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish(); // This will close the current activity and return to the previous one
        });
    }

    private void searchEvents(String searchText) {
        showLoading();
        Log.d(TAG, "Searching for: " + searchText);
        Query query = db.collection("Events")
                .orderBy("Name")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .limit(20);

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
