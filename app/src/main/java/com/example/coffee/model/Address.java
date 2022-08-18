package com.example.coffee.model;

public class Address {
    String id,nameAddress,phoneAddress,address,isDefault;

    public Address() {
    }

    public Address(String id, String nameAddress, String phoneAddress, String address, String isDefault) {
        this.id = id;
        this.nameAddress = nameAddress;
        this.phoneAddress = phoneAddress;
        this.address = address;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAddress() {
        return nameAddress;
    }

    public void setNameAddress(String nameAddress) {
        this.nameAddress = nameAddress;
    }

    public String getPhoneAddress() {
        return phoneAddress;
    }

    public void setPhoneAddress(String phoneAddress) {
        this.phoneAddress = phoneAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
