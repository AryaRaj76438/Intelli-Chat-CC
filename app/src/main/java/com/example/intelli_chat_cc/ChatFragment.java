package com.example.intelli_chat_cc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intelli_chat_cc.Adapter.RecentChatAdapter;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

    RecentChatAdapter recentChatAdapter;
    RecyclerView recyclerView;
    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recylcerViewFragmentChat);

        recentChatRecyclerView();
        return  view;
    }

    private void recentChatRecyclerView(){
        Query query = FirebaseUtils.getAllChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtils.getCurrentUserID())
                .orderBy("lastMessageTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new  FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query, ChatRoomModel.class).build();

        recentChatAdapter = new RecentChatAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recentChatAdapter);
        recentChatAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recentChatAdapter!=null) recentChatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(recentChatAdapter!=null) recentChatAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recentChatAdapter!=null) recentChatAdapter.onDataChanged();
    }
}