package com.example.intelli_chat_cc.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

@Entity
public class UserModel {
    @PrimaryKey
    @NonNull
    private String userId;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profilePicsUrl;
    private Status status;
    private Timestamp timeAdded;

    public UserModel(){}

    public UserModel(String userId, String username, String email,
                     String password, String phoneNumber, String profilePicsUrl,
                     Status status, Timestamp timeAdded) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profilePicsUrl = profilePicsUrl;
        this.status = status;
        this.timeAdded = timeAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicsUrl() {
        return profilePicsUrl;
    }

    public void setProfilePicsUrl(String profilePicsUrl) {
        this.profilePicsUrl = profilePicsUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
