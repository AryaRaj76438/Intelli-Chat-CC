package com.example.intelli_chat_cc.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelli_chat_cc.R;
import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.example.intelli_chat_cc.models.MessageModel;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MessageRecyclerAdapter extends FirestoreRecyclerAdapter<MessageModel, MessageRecyclerAdapter.ViewModel> {
    Context context;
    UserModel userModel;

    public MessageRecyclerAdapter(@NonNull FirestoreRecyclerOptions<MessageModel> options, Context context, UserModel usermodel) {
        super(options);
        this.context = context;
        this.userModel = usermodel;
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

        holder.itemView.setOnLongClickListener(v -> {
            String message = getSnapshots().get(position).getMessage();
            AndroidUtils.ToastMessage(context, message+position);
            deleteMessage(context,position);
            return true;
        });
    }

    @NonNull
    @Override
    public MessageRecyclerAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false);
        return new ViewModel(view);
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        LinearLayout rightLayout, leftLayout;
        RelativeLayout messageBox;
        TextView sender, receiver;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.messageSender);
            receiver = itemView.findViewById(R.id.messageReceiver);
            leftLayout = itemView.findViewById(R.id.leftChatLayout);
            rightLayout = itemView.findViewById(R.id.rightChatLayout);
            messageBox = itemView.findViewById(R.id.messageBoxRowLayout);
        }
    }


    private void deleteMessage(Context context, int position) {
        String chatroomId = FirebaseUtils.getChatRoomId(FirebaseUtils.getCurrentUserID(), userModel.getUserId());
        CollectionReference collectionReference = FirebaseUtils.getChatRoomMessageReference(chatroomId);
        String documentId = getSnapshots().getSnapshot(position).getId();
        collectionReference.document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                    if(position == 0){
                        updateLastMessage(chatroomId);
                    }

                    AndroidUtils.ToastMessage(context, "Message deleted");
                })
                .addOnFailureListener(e -> {
                    AndroidUtils.ToastMessage(context, "Error deleting message");
                });
    }

    private void updateLastMessage(String chatroomId) {
        CollectionReference collectionReference = FirebaseUtils.getChatRoomMessageReference(chatroomId);
        collectionReference.orderBy("messageTime", Query.Direction.DESCENDING)
                .limit(1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String message = snapshot.getString("message");
                        String messageSenderId = snapshot.getString("messageSenderId");
                        Timestamp messageTime = snapshot.getTimestamp("messageTime");

                        updateChatRoomModel(context,chatroomId,message, messageSenderId, messageTime);
                    }
                }).addOnFailureListener(e -> {

                });
    }

    private void updateChatRoomModel(Context context, String chatroomId, String message, String messageSenderId, Timestamp messageTime) {
        FirebaseUtils.getChatRoomReference(chatroomId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        ChatRoomModel chatRoomModel = documentSnapshot.toObject(ChatRoomModel.class);
                        if(chatRoomModel!=null){
                            chatRoomModel.setLastMessageTime(messageTime);
                            chatRoomModel.setLastMessage(message);
                            chatRoomModel.setLastMesssageSenderId(messageSenderId);

                            FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoomModel)
                                    .addOnSuccessListener(unused -> AndroidUtils.ToastMessage(context,"Message deleted"))
                                    .addOnFailureListener(e -> AndroidUtils.ToastMessage(context, "Message not deleted"));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("updateChatRoomModel", "updateChatRoomModel: "+e.getMessage());
                });
    }

}
