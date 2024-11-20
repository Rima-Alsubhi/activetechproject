package com.example.activetechproject;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        // عرض الاسم والنص
        holder.username.setText(message.getSenderId());
        holder.messageText.setText(message.getMessage());

        // عرض الصورة إذا كانت موجودة في الرسالة
//        if (message.getImageUri() != null) {
//            holder.messageImage.setVisibility(View.VISIBLE);
//            holder.messageImage.setImageURI(message.getImageUri());
//        } else {
//            holder.messageImage.setVisibility(View.GONE); // إخفاء الصورة إذا لم تكن جزء من الرسالة
//        }
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