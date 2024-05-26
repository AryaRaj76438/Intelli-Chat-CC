package com.example.intelli_chat_cc.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static String getCurrentUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static CollectionReference getAllUserCollectionReference(){
        return firestore.collection("users");
    }

    public static DocumentReference currentUserDetails(){
        return firestore.collection("users").document(getCurrentUserID());
    }
}
