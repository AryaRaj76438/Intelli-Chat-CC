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
import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChatAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatAdapter.ViewHolder> {
    Context context;
    public RecentChatAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtils.retrieveOtherUserFromChatroom(model.getUserIds()).get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       // Fetch other model
                       UserModel otherUser = task.getResult().toObject(UserModel.class);
                       boolean lastMessageSender = model.getLastMesssageSenderId().equals(FirebaseUtils.getCurrentUserID());

                       assert otherUser != null;

                       holder.usernameTxt.setText(otherUser.getUsername());
                       holder.lastMessageTimeTxt.setText(FirebaseUtils.timeStampToString(model.getLastMessageTime()));
                       String lastMessage = model.getLastMessage();
                       if(model.getLastMessage().length()>10){
                           lastMessage = lastMessage.substring(0,10)+"...";
                       }
                       if(lastMessageSender){
                           holder.lastMessageTxt.setText("You: "+lastMessage);
                       }else{
                           String tempSender = otherUser.getUsername();
                           if(tempSender.length()>5) {
                               tempSender = tempSender.substring(0, 5);
                           }
                           holder.lastMessageTxt.setText(tempSender+": "+lastMessage);
                       }

                       holder.itemView.setOnClickListener(
                               v->{
                                   Intent intent = new Intent(context, ChatActivity.class);
                                   AndroidUtils.passUserModelAsIntent(intent,otherUser);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   context.startActivity(intent);
                               }
                       );
                   }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_row, parent, false);
        return new ViewHolder(view);
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
}
