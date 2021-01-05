package com.example.sportconnect.model.user;

public class Users {
    private String userId;
    private String userName;
    private String userPhone;
    private String email;
    private String imageProfile;
    private String imageCover;
    private String dateOfBirth;
    private String gender;
    private String status;
    private String bio;

    public Users() {
    }

    public Users(String userId, String userName, String userPhone, String email, String imageProfile, String imageCover, String dateOfBirth, String gender, String status, String bio) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.email = email;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
        this.bio = bio;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
