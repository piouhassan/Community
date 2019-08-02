package com.pollen.community.Models;

public class SearchModel {
    int uid;
    String  name;
    String price;
    String cover;
    String description;
    String rating;
    int categoryId;
    int restaurantId;

    public SearchModel() {
    }

    public SearchModel(int uid, String name, String price, String cover, String description, String rating, int categoryId, int restaurantId) {
        this.uid = uid;
        this.name = name;
        this.price = price;
        this.cover = cover;
        this.description = description;
        this.rating = rating;
        this.categoryId = categoryId;
        this.restaurantId = restaurantId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
