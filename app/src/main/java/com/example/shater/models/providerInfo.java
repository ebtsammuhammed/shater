package com.example.shater.models;


import java.io.Serializable;


public class providerInfo implements Serializable {
    public String name;
    public String email;
    public String password;
    public String user;
    public String phone_number;
    public String id;
    public String category;
    public String experience ;
    public int accept_Admin;
    public int startRate ;
    public String idImage;

    public providerInfo() {
    }

    public providerInfo(String name, String email, String password, String user, String phone_number, String id, String category, String experience, int accept_Admin, int startRate, String idImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.user = user;
        this.phone_number = phone_number;
        this.id = id;
        this.category = category;
        this.experience = experience;
        this.accept_Admin = accept_Admin;
        this.startRate = startRate;
        this.idImage = idImage;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getAccept_Admin() {
        return accept_Admin;
    }

    public void setAccept_Admin(int accept_Admin) {
        this.accept_Admin = accept_Admin;
    }

    public int getStartRate() {
        return startRate;
    }

    public void setStartRate(int startRate) {
        this.startRate = startRate;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }
}
