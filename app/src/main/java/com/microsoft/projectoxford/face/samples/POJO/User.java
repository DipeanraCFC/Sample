package com.microsoft.projectoxford.face.samples.POJO;

/**
 * Created by Sushsant Khatiwada on 12/27/2016.
 * Object banauda jaile CONSTRUCTUR ko through bata pass hunu parcha
 * getter ra setter ni hunu parcha
 */

public class User {

    String email, id, address, phone;

    //alt+insert
    public User(String email, String id, String address, String phone) {
        this.email = email;
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
