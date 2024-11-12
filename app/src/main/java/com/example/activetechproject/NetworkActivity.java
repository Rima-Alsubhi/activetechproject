package com.example.activetechproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        setContentView(R.layout.activity_network);
        ImageButton backButton1 = findViewById(R.id.back_button);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to Home page
            }
        });


        Button btnCyber = findViewById(R.id.btn_cyber);
        btnCyber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity("Cyber Community");
            }
        });

        Button btnData = findViewById(R.id.btn_data);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity("Data Community");
            }
        });

        Button btnSoftware = findViewById(R.id.btn_software);
        btnSoftware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity("Software Community");
            }
        });

        }

    private void openChatActivity(String community) {
        Intent intent = new Intent(NetworkActivity.this, ChatActivity.class);
        intent.putExtra("community_name", community);
        startActivity(intent);

    }


}
