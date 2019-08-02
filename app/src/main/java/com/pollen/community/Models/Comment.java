package com.pollen.community.Models;

import com.pollen.community.UrlConfig.AppConfig;

public class Comment {

    String rating;
    String commentaire;
    String userCover;
    String firstname;
    String lastname;

    public Comment() {
    }

    public Comment(String rating, String commentaire, String userCover, String firstname, String lastname) {
        this.rating = rating;
        this.commentaire = commentaire;
        this.userCover = userCover;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getUserCover() {
        return AppConfig.URL_PICTURE+userCover;
    }

    public void setUserCover(String userCover) {
        this.userCover = userCover;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
