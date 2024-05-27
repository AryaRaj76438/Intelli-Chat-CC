package com.example.intelli_chat_cc.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.intelli_chat_cc.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

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

    // ChatRoom
    public static CollectionReference getAllChatroomCollectionReference(){
        return firestore.collection("chatrooms");
    }
    public static DocumentReference getChatRoomReference(String chatRoomId){
        return firestore.collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatroomId){
        return getChatRoomReference(chatroomId).collection("chats");
    }

    public static String getChatRoomId(String user1Id, String user2Id){
        if(user1Id.hashCode()<user2Id.hashCode()){
            return user1Id+"_"+user2Id;
        }else{
            return user2Id+"_"+user1Id;
        }
    }

    public static DocumentReference retrieveOtherUserFromChatroom(List<String> userIds){
        if(userIds.get(0).equals(getCurrentUserID())){
            return getAllUserCollectionReference().document(userIds.get(1));
        }else{
            return getAllUserCollectionReference().document(userIds.get(0));
        }
    }

    public static void logoutUser(){
        FirebaseAuth.getInstance().signOut();
    }

    public static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }


}
