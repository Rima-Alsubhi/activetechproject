package com.example.activetechproject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail;
    private Button editNameButton;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }

            return false;
        });

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        editNameButton = findViewById(R.id.editNameButton);

        // User ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        //Firebase
        loadUserData();

        editNameButton.setOnClickListener(v -> showEditNameDialog());
    }

    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("fullName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // TextViews
                    profileName.setText(name);
                    profileEmail.setText(email);
                } else {
                    Toast.makeText(ProfileActivity.this, "No user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to retrieve data:" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit The Name");

        final EditText input = new EditText(this);
        input.setHint("Enter The New Name");
        builder.setView(input);

        builder.setPositiveButton("Done", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            updateNameInFirebase(newName);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateNameInFirebase(String newName) {
        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a new name.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("fullName").setValue(newName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                profileName.setText(newName); //
                Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update name" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
