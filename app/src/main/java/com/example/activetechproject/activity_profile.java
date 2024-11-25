package com.example.activetechproject;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_profile extends AppCompatActivity {
    private TextView profileName, profileEmail;
    private Button editNameButton;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        editNameButton = findViewById(R.id.editNameButton);

        // استرجاع UID المستخدم
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // استرجاع البيانات من Firebase
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

                    // تعيين البيانات في TextViews
                    profileName.setText(name);
                    profileEmail.setText(email);
                } else {
                    Toast.makeText(activity_profile.this, "لا توجد بيانات مستخدم", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity_profile.this, "فشل في استرجاع البيانات: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تعديل الاسم");

        // إعداد EditText لإدخال الاسم الجديد
        final EditText input = new EditText(this);
        input.setHint("أدخل الاسم الجديد");
        builder.setView(input);

        builder.setPositiveButton("تأكيد", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            updateNameInFirebase(newName);
        });
        builder.setNegativeButton("إلغاء", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateNameInFirebase(String newName) {
        if (newName.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال اسم جديد", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("fullName").setValue(newName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                profileName.setText(newName); // تحديث الاسم المعروض
                Toast.makeText(this, "تم تحديث الاسم!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "فشل في تحديث الاسم: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
