package com.pollen.community.Models;

import android.widget.ImageView;

import com.pollen.community.UrlConfig.AppConfig;

public class Restaurant {

    private int uid;
    private String name;
    private String logo;
    private String manager_name;
    private String manager_phone;
    private String email;
    private String phone;
    private String address;
    private String time_open;
    private  int rating;
    private String type;
    private String description;
    private String created;

    public Restaurant() {
    }


    public Restaurant(String name, String logo, String address, String time_open, int rating, String type , int uid) {
        this.name = name;
        this.logo = logo;
        this.address = address;
        this.time_open = time_open;
        this.rating = rating;
        this.type = type;
        this.uid = uid;
    }
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return AppConfig.URL_PICTURE + logo;
    }


    public String getPhone() {
        return phone;
    }


    public String getTime_open() {
        return time_open;
    }

    public int getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(String created) {
        this.created = created;
    }


}
