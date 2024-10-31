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


        private EditText emailEditText;
        private FirebaseAuth mAuth;
        private FirebaseFirestore db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot_password);

            emailEditText = findViewById(R.id.email_forgot_password);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
            resetPasswordButton.setOnClickListener(v -> resetPassword());

//        Button homeButton = findViewById(R.id.homeButton);
//        homeButton.setOnClickListener(v -> goToHomeActivity());

            ImageButton backButton = findViewById(R.id.back_button2);
            backButton.setOnClickListener(v -> {
                Intent intent = new Intent(forgotPassword.this, MainActivity.class);
                startActivity(intent);
                finish();
            });


        }

        private void resetPassword() {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Please provide a valid email");
                emailEditText.requestFocus();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(forgotPassword.this,
                                    "Check your email to reset your password",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(forgotPassword.this,
                                    "Failed to send reset email: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }

        private void goToHomeActivity() {
            startActivity(new Intent(forgotPassword.this, HomeActivity.class));
            finish();
        }
    }
