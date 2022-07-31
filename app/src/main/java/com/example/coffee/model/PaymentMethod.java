package com.example.coffee.model;

public class PaymentMethod {
   private String paymentMethod;
   private int img;

    public PaymentMethod() {
    }

    public PaymentMethod(String paymentMethod, int img) {
        this.paymentMethod = paymentMethod;
        this.img = img;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
