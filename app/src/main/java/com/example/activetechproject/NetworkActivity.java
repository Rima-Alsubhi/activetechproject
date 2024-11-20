package com.example.activetechproject;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NetworkActivity extends AppCompatActivity {
    private Button btnCyber, btnData, btnSoftware;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        ImageButton backButton1 = findViewById(R.id.back_button);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to Home page
            }
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
        intent.putExtra("COMMUNITY_ID", communityId); // تمرير معرف المجتمع
        startActivity(intent);
    }
}