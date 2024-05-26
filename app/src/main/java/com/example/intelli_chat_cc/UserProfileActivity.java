package com.example.intelli_chat_cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.databinding.ActivityUserProfileBinding;
import com.example.intelli_chat_cc.models.Status;
import com.example.intelli_chat_cc.models.UserModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // From last Intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        getUserProfile();
        binding.saveProfileBtn.setOnClickListener(v-> {
            setUserDetails();
        });
    }

    private void setUserDetails() {
        String email = binding.emailProfileEdt.getText().toString().trim();
        String fullName = binding.fullNameProfileEdt.getText().toString().trim();
        String userId = FirebaseUtils.getCurrentUserID();
        String profileImageUrl = "";
        Status userCurrentStatus = Status.ONLINE;
        String password = "";

        if(email.length()<1){
            binding.emailProfileEdt.setError("Enter Email");
            return;
        }
        if(fullName.length()<3){
            binding.fullNameProfileEdt.setError("Fullname should be atleast 3 chars");
            return;
        }

        Log.d("PROFILEUSER", "setUserDetails: "+fullName+" "+email);

        if(userModel!=null){
            userModel.setEmail(email);
            userModel.setUsername(fullName);
        }else{
            userModel = new UserModel(userId,fullName,email,password,phoneNumber,profileImageUrl,userCurrentStatus, Timestamp.now());
        }

        Log.d("USER_PROFILE", "setUserDetails: "+userModel.getUsername()+" "+userModel+" \n"+userModel.getUsername());
        CollectionReference collectionReference = FirebaseUtils.getAllUserCollectionReference();
        Log.d("Profile:", "setUserDetails: "+collectionReference);


        // Save this model to firebase
        FirebaseUtils.currentUserDetails().set(userModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(e -> {
                    AndroidUtils.ToastMessage(UserProfileActivity.this,"Profile not updated\nTry Again!");
                    Log.d("USER PROFILE", "setUserDetails: "+e);
                });

    }

    private void getUserProfile() {
        FirebaseUtils.currentUserDetails().get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        userModel = task.getResult().toObject(UserModel.class);
                        if(userModel!=null){
                            binding.fullNameProfileEdt.setText(userModel.getUsername());
                            binding.emailProfile.setText(userModel.getEmail());
                            binding.emailProfileEdt.setText(userModel.getEmail());
                            binding.userIdProfile.setText(userModel.getUserId());
                        }
                    }
                });
    }
}

