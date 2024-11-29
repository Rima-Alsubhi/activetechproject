package com.example.activetechproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
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


        ImageButton exploreButton = findViewById(R.id.exploreButton);
        ImageButton networkButton = findViewById(R.id.networkButton);
//        ImageButton profileButton = findViewById(R.id.profileButton);

        exploreButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
            startActivity(intent);
        });

        networkButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NetworkActivity.class);
            startActivity(intent);
        });

//        profileButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        });
    }
}
