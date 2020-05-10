package com.example.shater.models;

public class saveMyOffer {
    String id_customer ;
    String name_customer;
    String email_customer;
    String phone_customer;
    String category ;
    String Date ;
    String Time ;
    String description;
    Double lat;
    Double lng;
    String  urlImage ;
    float price ;
    int accpet_customer ;

    public saveMyOffer(String id_customer, String name_customer, String email_customer, String phone_customer, String category, String date, String time, String description, Double lat, Double lng, String urlImage, float price, int accpet_customer) {
        this.id_customer = id_customer;
        this.name_customer = name_customer;
        this.email_customer = email_customer;
        this.phone_customer = phone_customer;
        this.category = category;
        Date = date;
        Time = time;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.urlImage = urlImage;
        this.price = price;
        this.accpet_customer = accpet_customer;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getName_customer() {
        return name_customer;
    }

    public void setName_customer(String name_customer) {
        this.name_customer = name_customer;
    }

    public String getEmail_customer() {
        return email_customer;
    }

    public void setEmail_customer(String email_customer) {
        this.email_customer = email_customer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAccpet_customer() {
        return accpet_customer;
    }

    public void setAccpet_customer(int accpet_customer) {
        this.accpet_customer = accpet_customer;
    }

    public String getPhone_customer() {
        return phone_customer;
    }

    public void setPhone_customer(String phone_customer) {
        this.phone_customer = phone_customer;
    }
}
