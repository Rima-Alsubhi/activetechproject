package com.example.activetechproject;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        holder.username.setText(message.getSenderId());
        holder.messageText.setText(message.getMessage());

        if (message.getImageUri() != null) {
            Log.d("ChatAdapter", "Image URL: " + message.getImageUri());
            holder.messageImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(message.getImageUri())
                    .into(holder.messageImage);
        } else {
            holder.messageImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView username, messageText;
        ImageView messageImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            messageText = itemView.findViewById(R.id.tv_message);
            messageImage = itemView.findViewById(R.id.message_image);
        }
    }
}
