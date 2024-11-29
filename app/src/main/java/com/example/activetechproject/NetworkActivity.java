package com.example.activetechproject;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NetworkActivity extends AppCompatActivity {
    private Button btnCyber, btnData, btnSoftware;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });

        btnCyber = findViewById(R.id.btn_cyber);
        btnData = findViewById(R.id.btn_data);
        btnSoftware = findViewById(R.id.btn_software);

        btnCyber.setOnClickListener(v -> openChat("cyber_community_id"));
        btnData.setOnClickListener(v -> openChat("data_community_id"));
        btnSoftware.setOnClickListener(v -> openChat("software_community_id"));
    }

    private void openChat(String communityId) {
        Intent intent = new Intent(NetworkActivity.this, ChatActivity.class);
        intent.putExtra("COMMUNITY_ID", communityId);
        startActivity(intent);
    }
}