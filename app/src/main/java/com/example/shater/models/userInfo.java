package com.example.shater.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class userInfo implements Serializable {

    public String name;
    public String email;
    public String password;
    public String user;
    public String phone_number;
    public String id;

    public userInfo() {
    }

    public userInfo(String name, String email, String password, String user, String phone_number,String id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.user = user;
        this.phone_number = phone_number;
        this.id = id ;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
