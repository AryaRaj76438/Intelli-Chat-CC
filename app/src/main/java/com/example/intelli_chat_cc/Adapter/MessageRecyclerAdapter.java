package com.example.intelli_chat_cc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelli_chat_cc.R;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.MessageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Objects;

public class MessageRecyclerAdapter extends FirestoreRecyclerAdapter<MessageModel, MessageRecyclerAdapter.ViewModel> {
    Context context;
    public MessageRecyclerAdapter(@NonNull FirestoreRecyclerOptions<MessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageRecyclerAdapter.ViewModel holder, int position, @NonNull MessageModel model) {
        if(Objects.equals(model.getMessageSenderId(), FirebaseUtils.getCurrentUserID())){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.sender.setText(model.getMessage().trim());
        }else{
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.receiver.setText(model.getMessage().trim());
        }
    }

    @NonNull
    @Override
    public MessageRecyclerAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false);
        return new ViewModel(view);
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        LinearLayout rightLayout, leftLayout;
        TextView sender, receiver;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.messageSender);
            receiver = itemView.findViewById(R.id.messageReceiver);
            leftLayout = itemView.findViewById(R.id.leftChatLayout);
            rightLayout = itemView.findViewById(R.id.rightChatLayout);
        }
    }
}
