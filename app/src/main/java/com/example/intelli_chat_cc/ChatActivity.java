package com.example.intelli_chat_cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.intelli_chat_cc.Adapter.MessageRecyclerAdapter;
import com.example.intelli_chat_cc.Adapter.SmartReplyAdapter;
import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.databinding.ActivityChatBinding;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.example.intelli_chat_cc.models.MessageModel;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String chatRoomId;
    ChatRoomModel chatRoomModel;
    UserModel otherUser;
    private Toolbar toolbar;

    MessageRecyclerAdapter adapter;

// SmartReply
    private SmartReplyAdapter smartReplyAdapter;
    private List<TextMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        toolbar = findViewById(R.id.appbarToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
        });

        otherUser = AndroidUtils.getUserModelFromIntent(getIntent());
        Objects.requireNonNull(getSupportActionBar()).setTitle(otherUser.getUsername());


        chatRoomId = FirebaseUtils.getChatRoomId(FirebaseUtils.getCurrentUserID(), otherUser.getUserId());
        // SetUp Chat Screen

        // Initialize smart reply RecyclerView
        binding.smartReplyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        smartReplyAdapter = new SmartReplyAdapter(new ArrayList<>(), this::onSmartReplyClicked);
        binding.smartReplyRecyclerView.setAdapter(smartReplyAdapter);


        binding.sendBtnChat.setOnClickListener(v-> sendMessageToUser());
        binding.smartReplyBtn.setOnClickListener(v -> {
            if (binding.smartReplyerLinearLayout.getVisibility() == View.VISIBLE) {
                binding.smartReplyerLinearLayout.setVisibility(View.GONE);
            } else {
                binding.smartReplyerLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        getOrCreateChatroomModel();
        chatRecyclerViewSetUp();
        setUpSmartReplyFeature();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_act_audio_video, menu);
        return true;
    }

    void getOrCreateChatroomModel(){
        FirebaseUtils.getChatRoomReference(chatRoomId).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        chatRoomModel = task.getResult().toObject(ChatRoomModel.class); // if already created chatroom
                        if(chatRoomModel==null){
                            chatRoomModel= new ChatRoomModel(
                                    chatRoomId, Arrays.asList(FirebaseUtils.getCurrentUserID(),otherUser.getUserId()),
                                    Timestamp.now(), "",""
                            );

                            FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel); // add chatroom to database
                        }
                    }
                });
    }


    private void sendMessageToUser(){
        String message = binding.messageBoxChat.getText().toString();
        if(message.isEmpty()){
            return;
        }
        // Setting the data for last Message send
        chatRoomModel.setLastMessage(message);
        chatRoomModel.setLastMessageTime(Timestamp.now());
        chatRoomModel.setLastMesssageSenderId(FirebaseUtils.getCurrentUserID());

        // Chatroom message model
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(message);
        messageModel.setMessageTime(Timestamp.now());
        messageModel.setMessageSenderId(FirebaseUtils.getCurrentUserID());
        messageModel.setText(true);

        // Chatroom reference
        FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);
        FirebaseUtils.getChatRoomMessageReference(chatRoomId).add(messageModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        binding.messageBoxChat.setText("");

                    }
                });
    }

    // Setting recyclerView in proper order
    private void chatRecyclerViewSetUp(){
        Query query = FirebaseUtils.getChatRoomMessageReference(chatRoomId)
                .orderBy("messageTime", Query.Direction.DESCENDING);

        // With all the message build recyclerView
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class).build();

        adapter = new MessageRecyclerAdapter(options, getApplicationContext(), otherUser);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        binding.messageRecyclerBoxChat.setLayoutManager(manager);
        binding.messageRecyclerBoxChat.setAdapter(adapter);
        adapter.startListening();

        adapter.registerAdapterDataObserver(
                new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        binding.messageRecyclerBoxChat.smoothScrollToPosition(0);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    private void setUpSmartReplyFeature() {
        SmartReplyGenerator smartReply = SmartReply.getClient();

        FirebaseUtils.getChatRoomMessageReference(chatRoomId).addSnapshotListener((snapshots, e) -> {
            if (e != null || snapshots == null) {
                Log.w("ChatActivity", "listen:error", e);
                return;
            }

            chatMessages.clear();
            for (DocumentSnapshot doc : snapshots) {
                MessageModel message = doc.toObject(MessageModel.class);
                if (message != null) {
                    chatMessages.add(TextMessage.createForRemoteUser(
                            message.getMessage(),
                            message.getMessageTime().toDate().getTime(),
                            message.getMessageSenderId()
                    ));
                }
            }
            chatMessages.sort((msg1, msg2) -> Long.compare(msg1.getTimestampMillis(), msg2.getTimestampMillis()));


            smartReply.suggestReplies(chatMessages)
                    .addOnSuccessListener(result -> {
                        if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            List<String> suggestions = new ArrayList<>();
                            for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                suggestions.add(suggestion.getText());
                            }
                            smartReplyAdapter.updateSuggestion(suggestions);
                        }
                    })
                    .addOnFailureListener(e1 -> Log.w("ChatActivity", "Smart reply failed.", e));
        });
    }


    private void onSmartReplyClicked(String reply) {
        binding.messageBoxChat.setText(reply);
    }
}