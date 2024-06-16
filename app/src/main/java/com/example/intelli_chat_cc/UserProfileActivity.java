package com.example.intelli_chat_cc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.intelli_chat_cc.Utils.AndroidUtils;
import com.example.intelli_chat_cc.Utils.CircleTransform;
import com.example.intelli_chat_cc.Utils.FirebaseUtils;
import com.example.intelli_chat_cc.databinding.ActivityUserProfileBinding;
import com.example.intelli_chat_cc.models.Status;
import com.example.intelli_chat_cc.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    private static final int  REQUEST_CODE_IMAGE = 100;
    ActivityUserProfileBinding binding;
    String phoneNumber;
    UserModel userModel;
    Uri ProfileImageUri;
    String imageUrlUpload = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.appbarToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User-Profile");
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });

        // From last Intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        getUserProfile();

        binding.profileImageProfile.setOnClickListener(v -> {
            selectImage();
        });

        binding.saveProfileBtn.setOnClickListener(v-> {
            setUserDetails();
        });

        binding.phoneNumProfile.setOnClickListener(v -> {
            final EditText editText = new EditText(v.getContext());
            editText.setHint("Enter new phone number");

            AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("Edit Phone Number")
                    .setMessage("Please enter your new phone number")
                    .setView(editText)
                    .setPositiveButton("OK", (dialog1, which) -> {
                        String newPhoneNumber = editText.getText().toString();
                        binding.phoneNumProfile.setText(newPhoneNumber);
                    })
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.show();
        });

    }

    private void setUserDetails() {
        // Profile Image
        if(ProfileImageUri != null){
            uploadProfileImage(ProfileImageUri, FirebaseUtils.getCurrentUserID());
        }

        String email = binding.emailProfileEdt.getText().toString().trim();
        String fullName = binding.fullNameProfileEdt.getText().toString().trim();
        String userId = FirebaseUtils.getCurrentUserID();
        String profileImageUrl = imageUrlUpload;
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

        if(userModel!=null){
            userModel.setEmail(email);
            userModel.setUsername(fullName);
            userModel.setProfilePicsUrl(imageUrlUpload);
        }else{
            userModel = new UserModel(userId,fullName,email,password,phoneNumber, profileImageUrl,userCurrentStatus, Timestamp.now());
        }

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
                            binding.phoneNumProfile.setText(userModel.getPhoneNumber());
                            binding.fullNameProfileEdt.setText(userModel.getUsername());
                            binding.emailProfile.setText(userModel.getEmail());
                            binding.emailProfileEdt.setText(userModel.getEmail());
                            binding.userIdProfile.setText(userModel.getUserId());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            Date profileCreatedDate = userModel.getTimeAdded().toDate();
                            String formattedDate = dateFormat.format(profileCreatedDate);
                            binding.profileCreatedDate.setText(formattedDate);

                            String pathProfileImage = "profileImage/"+FirebaseUtils.getCurrentUserID()+"/profileImage.jpg";
                            if(userModel.getProfilePicsUrl().length()>2){
                                    Picasso.get().load(userModel.getProfilePicsUrl())
                                            .fit()
                                            .transform(new CircleTransform())
                                            .into(binding.profileImageProfile);
                            }
                        }
                    }
                });
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    public void uploadProfileImage(Uri fileUri, String userId) {
        String pathProfileImage = "profileImage/"+userId+"/profileImage.jpg";
        StorageReference profileImagesRef = FirebaseUtils.getStorageReference().child(pathProfileImage);
        Log.d("PROFILE IMAGE", "uploadProfileImage: "+profileImagesRef);
        profileImagesRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    profileImagesRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                imageUrlUpload = uri.toString();
                                updateUserProfile();
                            });
                }).addOnFailureListener(error -> Log.d("PROFILE IMAGE ERROR", "onFailure: "+error));
    }

    public void updateFullName(String fullName){
        DocumentReference userRef = FirebaseUtils.currentUserDetails();
        userRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot userDetails = task.getResult();
                if(userDetails.exists()){
                    userRef.update("username", fullName)
                            .addOnSuccessListener(unused -> {
                                AndroidUtils.ToastMessage(UserProfileActivity.this, "FullName Updated");
                            })
                            .addOnFailureListener(e -> {
                                AndroidUtils.ToastMessage(UserProfileActivity.this, "FullName is not Updated\nTry Again!");
                            });

                }else{
                    UserModel userModel1 = new UserModel(FirebaseUtils.getCurrentUserID(),
                            fullName, "", "", phoneNumber, "",
                            Status.ONLINE, Timestamp.now());
                    userRef.set(userModel1)
                            .addOnSuccessListener(unused -> AndroidUtils.ToastMessage(UserProfileActivity.this, "Updated"))
                            .addOnFailureListener(e -> AndroidUtils.ToastMessage(UserProfileActivity.this, "Error: "+e.getMessage()));
                }
            }
            else {
                AndroidUtils.ToastMessage(UserProfileActivity.this, "Failed to get user document"+task.getException());
            }
        });

        if(userModel!=null){
            userModel.setUsername(fullName);
        }else{
            userModel = new UserModel(FirebaseUtils.getCurrentUserID(),
                    fullName, "", "", phoneNumber, "",
                    Status.ONLINE, Timestamp.now());
        }

    }

    public void updateUserProfile(){
        String email = binding.emailProfileEdt.getText().toString().trim();
        String fullName = binding.fullNameProfileEdt.getText().toString().trim();
        String userId = FirebaseUtils.getCurrentUserID();
        String profileImageUrl = imageUrlUpload;
        Status userCurrentStatus = Status.ONLINE;
        String password = "";

        if(email.isEmpty()){
            binding.emailProfileEdt.setError("Enter Email");
            return;
        }
        if(fullName.length()<3){
            binding.fullNameProfileEdt.setError("Fullname should be atleast 3 chars");
            return;
        }

        if(userModel!=null){
            userModel.setEmail(email);
            userModel.setUsername(fullName);
            userModel.setProfilePicsUrl(imageUrlUpload);
        }else{
            userModel = new UserModel(userId,fullName,email,password,phoneNumber, profileImageUrl,userCurrentStatus, Timestamp.now());
        }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data!=null && data.getData() !=null){
            ProfileImageUri = data.getData();
            binding.profileImageProfile.setImageURI(ProfileImageUri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}

