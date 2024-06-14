package com.example.intelli_chat_cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.intelli_chat_cc.Adapter.SearchUserAdapter;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.databinding.ActivitySearchUserBinding;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class SearchUserActivity extends AppCompatActivity {
    ActivitySearchUserBinding binding;
    SearchUserAdapter searchUserAdapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.appbarToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Search User");
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(SearchUserActivity.this, MainActivity.class));
            finish();
        });

        binding.searchUserBtn.setOnClickListener(v -> {
            String searchedUser = binding.searchUsernameEdt.getText().toString().trim();
            if(searchedUser.length()<3){
                binding.searchUsernameEdt.setError("Invalid User");
                return;
            }
            searchUserRecycler(searchedUser);
        });
    }

    private void searchUserRecycler(String searchedUser){
        Query query = FirebaseUtils.getAllUserCollectionReference()
                .whereGreaterThanOrEqualTo("username", searchedUser);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        searchUserAdapter = new SearchUserAdapter(options, getApplicationContext());
        binding.userSearchResult.setLayoutManager(new LinearLayoutManager(this));
        binding.userSearchResult.setAdapter(searchUserAdapter);
        searchUserAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(searchUserAdapter!=null) searchUserAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(searchUserAdapter!=null) searchUserAdapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchUserAdapter!=null) searchUserAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchUserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}