package com.example.intelli_chat_cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.example.intelli_chat_cc.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Connect tab layout with pager
        binding.tabLayout.setupWithViewPager(binding.pager);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainPagerAdapter.addFragment(new ChatFragment(), "Chats");
        mainPagerAdapter.addFragment(new StatusFragment(), "Status");
        binding.pager.setAdapter(mainPagerAdapter);
    }

}