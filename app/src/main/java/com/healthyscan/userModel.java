package com.healthyscan;

public class userModel {
    String username;
    String password;


    public userModel(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public userModel() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
