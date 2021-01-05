package com.example.sportconnect.model;

public class Chatlist {
    private String userId;
    private String userName;
    private String desc;
    private String date;
    private String urlProfile;

    public Chatlist() {
    }

    public Chatlist(String userId, String userName, String desc, String date, String urlProfile) {
        this.userId = userId;
        this.userName = userName;
        this.desc = desc;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
