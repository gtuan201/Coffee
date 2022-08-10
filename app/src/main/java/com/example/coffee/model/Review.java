package com.example.coffee.model;

public class Review {
    String imgUser,userName,imgReview,review,rating;

    public Review(String imgUser, String userName, String imgReview, String review, String rating) {
        this.imgUser = imgUser;
        this.userName = userName;
        this.imgReview = imgReview;
        this.review = review;
        this.rating = rating;
    }

    public Review() {
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgReview() {
        return imgReview;
    }

    public void setImgReview(String imgReview) {
        this.imgReview = imgReview;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
