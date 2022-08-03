package com.example.coffee.model;

public class Shop {
    private String imgUrlShop, address,name;

    public Shop() {
    }

    public Shop(String imgUrlShop, String address, String name) {
        this.imgUrlShop = imgUrlShop;
        this.address = address;
        this.name = name;
    }

    public String getImgUrlShop() {
        return imgUrlShop;
    }

    public void setImgUrlShop(String imgUrlShop) {
        this.imgUrlShop = imgUrlShop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
