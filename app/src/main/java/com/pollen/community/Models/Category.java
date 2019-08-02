package com.pollen.community.Models;

import com.pollen.community.UrlConfig.AppConfig;

public class Category {
    private String name;
    private  int Uid;
    private  String  category_url;
    private int restaurantId;
    public Category() {
    }

    public Category(String name, int uid, String category_url, int restaurantId) {
        this.name = name;
        Uid = uid;
        this.category_url = category_url;
        this.restaurantId = restaurantId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCategory_url() {
        return AppConfig.URL_PICTURE + category_url;
    }

    public void setCategory_url(String category_url) {
        this.category_url = category_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int uid) {
        Uid = uid;
    }
}
