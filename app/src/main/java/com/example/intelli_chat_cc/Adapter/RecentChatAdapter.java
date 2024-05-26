package com.example.intelli_chat_cc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelli_chat_cc.ChatActivity;
import com.example.intelli_chat_cc.R;
import com.example.intelli_chat_cc.models.ChatRoomModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {
    private final ArrayList<ChatRoomModel> chatRoomModelArray = new ArrayList<>();
    Context context;

    public RecentChatAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public RecentChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.recent_chat_fragment,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatAdapter.ViewHolder holder, int position) {

        // Assumption it's user
        ChatRoomModel chatRoom = chatRoomModelArray.get(position);
        holder.lastMessageTxt.setText(chatRoom.getLastMessage());
        holder.usernameTxt.setText("Arya");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return chatRoomModelArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTxt;
        TextView lastMessageTxt;
        TextView lastMessageTimeTxt;
        ImageView profilePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTxt = itemView.findViewById(R.id.recent_row_username);
            lastMessageTxt = itemView.findViewById(R.id.recent_row_last_message);
            lastMessageTimeTxt = itemView.findViewById(R.id.recent_row_last_messagetime);
            profilePic = itemView.findViewById(R.id.recent_row_profile_image);
        }
    }

    public void addChatRoomModel(ChatRoomModel chatRoomModel){
        chatRoomModelArray.add(chatRoomModel);
    }
}
