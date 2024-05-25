package com.example.intelli_chat_cc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intelli_chat_cc.Adapter.RecentChatAdapter;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.google.firebase.Timestamp;

import java.util.Arrays;


public class ChatFragment extends Fragment {

    RecentChatAdapter recentChatAdapter;
    RecyclerView recyclerView;
    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recylcerViewFragmentChat);

        recentChatAdapter = new RecentChatAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recentChatAdapter);

        recentChatAdapter.addChatRoomModel(new ChatRoomModel("1", Arrays.asList("user1", "user2"), Timestamp.now(), "user1", "Hello, how are you?"));
        recentChatAdapter.addChatRoomModel(new ChatRoomModel("2", Arrays.asList("user3", "user4"), Timestamp.now(), "user3", "What's up?"));

        return  view;
    }
}