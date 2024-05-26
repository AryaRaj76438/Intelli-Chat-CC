package com.example.intelli_chat_cc.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
    private String chatRoomId;
    private List<String> userIds;
    private Timestamp lastMessageTime;
    private String lastMesssageSenderId;
    private String lastMessage;

    public ChatRoomModel(){}
    public ChatRoomModel(String chatRoomId, List<String> userIds, Timestamp lastMessageTime, String lastMesssageSenderId, String lastMessage) {
        this.chatRoomId = chatRoomId;
        this.userIds = userIds;
        this.lastMessageTime = lastMessageTime;
        this.lastMesssageSenderId = lastMesssageSenderId;
        this.lastMessage = lastMessage;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMesssageSenderId() {
        return lastMesssageSenderId;
    }

    public void setLastMesssageSenderId(String lastMesssageSenderId) {
        this.lastMesssageSenderId = lastMesssageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
