package com.example.intelli_chat_cc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intelli_chat_cc.Adapter.AllUserAdapter;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class StatusFragment extends Fragment {
    AllUserAdapter allUserAdapter;
    RecyclerView allUserRecyclerView;
    public StatusFragment() {}
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        allUserRecyclerView = view.findViewById(R.id.allUserList);

        allUserListRecyclerView();
        return view;
    }
//
    private void allUserListRecyclerView() {
        Query query = FirebaseUtils.getAllUserCollectionReference();

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        allUserAdapter = new AllUserAdapter(options, getContext());
        allUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allUserRecyclerView.setAdapter(allUserAdapter);
        allUserAdapter.startListening();
    }

}