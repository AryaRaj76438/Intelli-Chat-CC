package com.example.intelli_chat_cc;

import androidx.appcompat.app.AppCompatActivity;
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

public class SearchUserActivity extends AppCompatActivity {
    ActivitySearchUserBinding binding;
    SearchUserAdapter searchUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v->startActivity(new Intent(SearchUserActivity.this, MainActivity.class)));
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
//                .orderBy("timeAdded", Query.Direction.DESCENDING);

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
}