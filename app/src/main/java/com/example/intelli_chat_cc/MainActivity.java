package com.example.intelli_chat_cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.example.intelli_chat_cc.Adapter.MainPagerAdapter;
import com.example.intelli_chat_cc.Adapter.RecentChatAdapter;
import com.example.intelli_chat_cc.databinding.ActivityMainBinding;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.example.intelli_chat_cc.models.Status;
import com.example.intelli_chat_cc.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating User
        UserModel user1 = new UserModel("1","user1", "user1@gmail.com",
                "123", "987", "user1", Status.ONLINE, null);
        UserModel user2 = new UserModel("2","user2", "user2@gmail.com",
                "123", "987", "user1", Status.ONLINE, null);

        List<String> userIds = new ArrayList<>();
        userIds.add(user1.getUserId()); userIds.add(user2.getUserId());
        ChatRoomModel chatRoomModel = new ChatRoomModel("user1", userIds, null, userIds.get(0), "Hey It's Arya" );
        ChatRoomModel chatRoomModel1 = new ChatRoomModel("user2", userIds, null, userIds.get(1), "Hey It's Arya2" );

        RecentChatAdapter recentChatAdapter = new RecentChatAdapter(this);
        recentChatAdapter.addChatRoomModel(chatRoomModel);
        recentChatAdapter.addChatRoomModel(chatRoomModel1);



        // Connect tab layout with pager
        binding.tabLayout.setupWithViewPager(binding.pager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainPagerAdapter.addFragment(new ChatFragment(), "Chats");
        mainPagerAdapter.addFragment(new StatusFragment(), "Status");
        binding.pager.setAdapter(mainPagerAdapter);







    }

}