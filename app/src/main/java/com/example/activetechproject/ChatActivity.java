package com.example.activetechproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private EditText messageInput;
    private Uri selectedImageUri;
    private String communityId; // تأكد من تعيين هذا المعرف من النشاط السابق


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.et_message);
        ImageButton sendButton = findViewById(R.id.btn_send);
        ImageButton attachmentButton = findViewById(R.id.btn_attachment);
        ImageButton backButton1 = findViewById(R.id.back_button);

        communityId = getIntent().getStringExtra("COMMUNITY_ID"); // الحصول على معرف المجتمع

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        loadMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if (!message.isEmpty() || selectedImageUri != null) {
                    sendMessage(message, selectedImageUri);
                    messageInput.setText("");
                    selectedImageUri = null; // إعادة تعيين الصورة بعد الإرسال
                }
                chatAdapter.notifyDataSetChanged(); // أبلغ adapter بتحديث البيانات
            }
        });


        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSourceDialog();
            }
        });

        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to Network page
            }
        });
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("test");
        database.setValue("Hello, Firebase!");


    }

    private void openImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            takePhoto(); // تفعيل الكاميرا
                        } else {
                            openImagePicker(); // فتح الاستديو
                        }
                    }
                });
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        // طلب إذن الكاميرا إذا لم يكن متاحًا
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto(); // حاول فتح الكاميرا مجددًا بعد الحصول على الإذن
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // احفظ صورة الكاميرا
        }
    }
    private void loadMessages() {
        // الحصول على الرسائل من قاعدة البيانات الخاصة بالمجتمع المحدد
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(communityId);
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear(); // مسح الرسائل السابقة
                // استرجاع الرسائل الجديدة
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage); // إضافة الرسالة إلى قائمة الرسائل

                }
                chatAdapter.notifyDataSetChanged(); // تحديث الشات لعرض الرسائل الجديدة
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1); // للتمرير إلى اخر رسالة
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to load messages: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String message, Uri imageUri) {
        // للحصول على اسم المستخدم الحالي
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // لاسترجاع اسم المستخدم من قاعدة البيانات
                String username = dataSnapshot.child("fullName").getValue(String.class);
                ChatMessage chatMessage = new ChatMessage(username, message, timestamp);
                chatMessages.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(communityId);
                String messageId = messagesRef.push().getKey();

                if (messageId != null) {
                    messagesRef.child(messageId).setValue(chatMessage).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this,
                        "Failed to read data " + databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}