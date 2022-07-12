package com.example.coffee.model;

public class Coffee {
    String urlImg, coffeeName, coffeeDescription, price, size, category,id,rate,background,location;

    public Coffee() {
    }

    public Coffee(String urlImg, String coffeeName, String coffeeDescription, String price, String size, String category, String id, String rate, String background, String location) {
        this.urlImg = urlImg;
        this.coffeeName = coffeeName;
        this.coffeeDescription = coffeeDescription;
        this.price = price;
        this.size = size;
        this.category = category;
        this.id = id;
        this.rate = rate;
        this.background = background;
        this.location = location;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public String getCoffeeDescription() {
        return coffeeDescription;
    }

    public void setCoffeeDescription(String coffeeDescription) {
        this.coffeeDescription = coffeeDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
