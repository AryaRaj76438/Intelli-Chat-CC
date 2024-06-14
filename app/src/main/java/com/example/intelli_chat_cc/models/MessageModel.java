package com.example.intelli_chat_cc.models;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String Message;
    private String messageSenderId;
    private Timestamp messageTime;
    private Boolean isText;

    public MessageModel() {}

    public MessageModel(String message, String messageSenderId, Timestamp messageTime, Boolean isText) {
        Message = message;
        this.messageSenderId = messageSenderId;
        this.messageTime = messageTime;
        this.isText = isText;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageSenderId() {
        return messageSenderId;
    }

    public void setMessageSenderId(String messageSenderId) {
        this.messageSenderId = messageSenderId;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public Boolean getText() {
        return isText;
    }

    public void setText(Boolean text) {
        isText = text;
    }
}
