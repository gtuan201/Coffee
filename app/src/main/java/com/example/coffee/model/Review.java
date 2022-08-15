package com.example.coffee.model;

public class Review {
    String id,imgUser,userName,imgReview,review,rating,date,time,coffeeName;


    public Review() {
    }

    public Review(String id, String imgUser, String userName, String imgReview, String review, String rating, String date, String time, String coffeeName) {
        this.id = id;
        this.imgUser = imgUser;
        this.userName = userName;
        this.imgReview = imgReview;
        this.review = review;
        this.rating = rating;
        this.date = date;
        this.time = time;
        this.coffeeName = coffeeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }
}
