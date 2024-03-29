package com.example.coffee.model;

import android.widget.LinearLayout;

import java.util.List;

public class Cart {
    String coffeeID,imgCart,nameCart,sizeCart,iceCart,quantityCart,totalPriceCart,noteCart,status;
    public Cart() {
    }

    public Cart(String coffeeID, String imgCart, String nameCart, String sizeCart, String iceCart, String quantityCart, String totalPriceCart, String noteCart, String status) {
        this.coffeeID = coffeeID;
        this.imgCart = imgCart;
        this.nameCart = nameCart;
        this.sizeCart = sizeCart;
        this.iceCart = iceCart;
        this.quantityCart = quantityCart;
        this.totalPriceCart = totalPriceCart;
        this.noteCart = noteCart;
        this.status = status;
    }

    public String getCoffeeID() {
        return coffeeID;
    }

    public void setCoffeeID(String coffeeID) {
        this.coffeeID = coffeeID;
    }

    public String getImgCart() {
        return imgCart;
    }

    public void setImgCart(String imgCart) {
        this.imgCart = imgCart;
    }

    public String getNameCart() {
        return nameCart;
    }

    public void setNameCart(String nameCart) {
        this.nameCart = nameCart;
    }

    public String getSizeCart() {
        return sizeCart;
    }

    public void setSizeCart(String sizeCart) {
        this.sizeCart = sizeCart;
    }

    public String getIceCart() {
        return iceCart;
    }

    public void setIceCart(String iceCart) {
        this.iceCart = iceCart;
    }

    public String getQuantityCart() {
        return quantityCart;
    }

    public void setQuantityCart(String quantityCart) {
        this.quantityCart = quantityCart;
    }

    public String getTotalPriceCart() {
        return totalPriceCart;
    }

    public void setTotalPriceCart(String totalPriceCart) {
        this.totalPriceCart = totalPriceCart;
    }

    public String getNoteCart() {
        return noteCart;
    }

    public void setNoteCart(String noteCart) {
        this.noteCart = noteCart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
