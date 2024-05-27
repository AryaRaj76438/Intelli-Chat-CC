package com.example.intelli_chat_cc.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.intelli_chat_cc.models.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtils {

    // Display the toast-message to context
    public static void ToastMessage(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }


    public static void passUserModelAsIntent(Intent intent, UserModel userModel){
        intent.putExtra("email", userModel.getEmail());
        intent.putExtra("username",userModel.getUsername());
        intent.putExtra("userId", userModel.getUserId());
        intent.putExtra("phoneNumber", userModel.getPhoneNumber());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setEmail(intent.getStringExtra("email"));
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

}
