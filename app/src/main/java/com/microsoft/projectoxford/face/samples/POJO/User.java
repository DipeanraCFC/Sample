package com.microsoft.projectoxford.face.samples.POJO;


public class User {

    public String email, address, phone, name;

    //alt+insert
    public User(String email, String address, String phone, String name) {
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;


    }
}
