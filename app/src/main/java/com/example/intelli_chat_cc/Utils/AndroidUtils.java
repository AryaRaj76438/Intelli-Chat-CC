package com.example.intelli_chat_cc.Utils;

import android.content.Context;
import android.widget.Toast;

public class AndroidUtils {

    // Display the toast-message to context
    public static void ToastMessage(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
