package com.pollen.community.Models;

import android.widget.ImageView;

public class User {

    private String phone;
    private  String username;
    private ImageView avatar;
    private String fullname;
    private String address;
    private String account;

    public User(String phone, String username, ImageView avatar, String fullname, String address, String account) {
        this.phone = phone;
        this.username = username;
        this.avatar = avatar;
        this.fullname = fullname;
        this.address = address;
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getAccount() {
        return account;
    }
}
