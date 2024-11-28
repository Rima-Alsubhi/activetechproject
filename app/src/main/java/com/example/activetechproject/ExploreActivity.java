package com.example.activetechproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private Spinner spinnerOrganization;
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView tvNoResults;
    private String selectedOrganization = ""; // للمنظمة المختارة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Binding UI components
        etSearchEvent = findViewById(R.id.et_search_event);
        spinnerOrganization = findViewById(R.id.filter);
        recyclerViewEvents = findViewById(R.id.recycler_view_events);
        progressBar = findViewById(R.id.progress_bar);
        tvNoResults = findViewById(R.id.tv_no_results);

        // Setting up RecyclerView
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);

        // Setup Spinner with organization options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.organization_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrganization.setAdapter(adapter);

        // Spinner item selection listener
        spinnerOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganization = parent.getItemAtPosition(position).toString();
                searchEvents(etSearchEvent.getText().toString()); // تحديث النتائج بناءً على المنظمة
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrganization = ""; // إعادة التعيين في حال عدم اختيار شيء
            }
        });

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
        Log.d(TAG, "Searching for: " + searchText + ", Organization: " + selectedOrganization);

        Query query = db.collection("Events");

        // Apply search text filter if not empty
        if (!searchText.isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("Name", searchText)
                    .whereLessThanOrEqualTo("Name", searchText + "\uf8ff");
        }

        // Apply organization filter if selected and not "All"
        if (!selectedOrganization.isEmpty() && !selectedOrganization.equals("All")) {
            query = query.whereEqualTo("organization", selectedOrganization);
        }

        query.limit(20).get().addOnCompleteListener(task -> {
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
/**import android.os.Bundle;
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
}**/
