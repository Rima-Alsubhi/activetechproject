package com.example.activetechproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class forgotPassword extends AppCompatActivity {

    private EditText emailEditText; // Input field for the user's email
    private FirebaseAuth mAuth; // Firebase Authentication instance for handling user authentication
    private FirebaseFirestore db; // Firebase Firestore instance (not used in the code but initialized)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize the email input field and Firebase instances
        emailEditText = findViewById(R.id.email_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up the "Reset Password" button and its click listener
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(v -> resetPassword());

        // Set up the back button to navigate back to the MainActivity
        ImageButton backButton = findViewById(R.id.back_button2);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(forgotPassword.this, MainActivity.class);
            startActivity(intent); // Start the MainActivity
            finish(); // Finish the current activity
        });
    }

    private void resetPassword() {
        // Retrieve the email input from the user
        String email = emailEditText.getText().toString().trim();

        // Check if the email field is empty
        if (email.isEmpty()) {
            emailEditText.setError("Email is required"); // Show error if email is empty
            emailEditText.requestFocus(); // Focus on the email field
            return;
        }

        // Validate the email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide a valid email"); // Show error if email format is invalid
            emailEditText.requestFocus(); // Focus on the email field
            return;
        }

        // Send a password reset email using Firebase Authentication
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Notify the user to check their email
                        Toast.makeText(forgotPassword.this,
                                "Check your email to reset your password",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Show an error message if the password reset email fails
                        Toast.makeText(forgotPassword.this,
                                "Failed to send reset email: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToHomeActivity() {
        // Navigate to the HomeActivity screen
        startActivity(new Intent(forgotPassword.this, HomeActivity.class));
        finish(); // Finish the current activity
    }
}
