package com.example.intelli_chat_cc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelli_chat_cc.ChatActivity;
import com.example.intelli_chat_cc.R;
import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.CircleTransform;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AllUserAdapter extends FirestoreRecyclerAdapter<UserModel, AllUserAdapter.ViewModel> {
    Context context;
    public AllUserAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AllUserAdapter.ViewModel holder, int position, @NonNull UserModel model) {
        holder.phonenum.setText(model.getPhoneNumber());

        if(model.getProfilePicsUrl().length()>2){
            Picasso.get().load(model.getProfilePicsUrl())
                    .transform(new CircleTransform())
                    .into(holder.profileImage);
        }
        if(model.getUserId().equals(FirebaseUtils.getCurrentUserID())){
            holder.username.setText(model.getUsername()+"(You)");
        }else{
            holder.username.setText(model.getUsername());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtils.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public AllUserAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new ViewModel(view);
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        TextView username, phonenum;
        ImageView profileImage;
        public ViewModel(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameSearchUserList);
            phonenum = itemView.findViewById(R.id.phoneNumSearchUserList);
            profileImage = itemView.findViewById(R.id.searchUserProfileImage);
        }
    }
}
