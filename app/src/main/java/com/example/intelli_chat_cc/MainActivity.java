package com.example.intelli_chat_cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.intelli_chat_cc.Adapter.MainPagerAdapter;
import com.example.intelli_chat_cc.Adapter.RecentChatAdapter;
import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.databinding.ActivityMainBinding;
import com.example.intelli_chat_cc.models.ChatRoomModel;
import com.example.intelli_chat_cc.models.Status;
import com.example.intelli_chat_cc.models.UserModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Action bar
        toolbar = findViewById(R.id.appbarToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Intelli-Chat");

        // Connect to pager
        binding.tabLayout.setupWithViewPager(binding.pager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainPagerAdapter.addFragment(new ChatFragment(), "Chats");
        mainPagerAdapter.addFragment(new StatusFragment(), "Status");
        binding.pager.setAdapter(mainPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profileSideMenu){
            startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
            return true;
        }else if (item.getItemId()==R.id.searchUserProfile){
            startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
        }
        else if (item.getItemId() == R.id.shareSideMenu) {
            AndroidUtils.ToastMessage(MainActivity.this, "Share");
            return true;
        }else if(item.getItemId() == R.id.logoutSideMenu){
            FirebaseUtils.logoutUser();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            AndroidUtils.ToastMessage(MainActivity.this, "Logout");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}