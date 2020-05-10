package com.example.shater.models;

import java.util.Comparator;

public class receiverFromProvider {

    String id_provider ;
    String id_customer ;
    String name_customer ;
    String phone_customer ;
    String id_offer;
    String name_provider ;
    String email_provider;
    String phone_number;
    String category ;
    String description;
    Double lat_provider;
    Double lng_provider;
    String  urlImage ;
    float munStartRating ;
    float price ;
    int accept_customer ;
    int pay_customer;
    int rating_customer ;
    String date ;
    public String experience_provider ;

    public receiverFromProvider() {
    }

    public receiverFromProvider(String id_provider, String id_customer, String name_customer, String phone_customer,
                                String id_offer, String name_provider, String email_provider, String phone_number,
                                String category, String description, Double lat_provider, Double lng_provider, String urlImage,
                                float munStartRating, float price, int accept_customer, int pay_customer, int rating_customer,
                                String date, String experience_provider) {
        this.id_provider = id_provider;
        this.id_customer = id_customer;
        this.name_customer = name_customer;
        this.phone_customer = phone_customer;
        this.id_offer = id_offer;
        this.name_provider = name_provider;
        this.email_provider = email_provider;
        this.phone_number = phone_number;
        this.category = category;
        this.description = description;
        this.lat_provider = lat_provider;
        this.lng_provider = lng_provider;
        this.urlImage = urlImage;
        this.munStartRating = munStartRating;
        this.price = price;
        this.accept_customer = accept_customer;
        this.pay_customer = pay_customer;
        this.rating_customer = rating_customer;
        this.date = date;
        this.experience_provider = experience_provider;
    }

    public String getId_provider() {
        return id_provider;
    }

    public void setId_provider(String id_provider) {
        this.id_provider = id_provider;
    }

    public String getName_provider() {
        return name_provider;
    }

    public void setName_provider(String name_provider) {
        this.name_provider = name_provider;
    }

    public String getEmail_provider() {
        return email_provider;
    }

    public void setEmail_provider(String email_provider) {
        this.email_provider = email_provider;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat_provider() {
        return lat_provider;
    }

    public void setLat_provider(Double lat_provider) {
        this.lat_provider = lat_provider;
    }

    public Double getLng_provider() {
        return lng_provider;
    }

    public void setLng_provider(Double lng_provider) {
        this.lng_provider = lng_provider;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public float getMunStartRating() {
        return munStartRating;
    }

    public void setMunStartRating(float munStartRating) {
        this.munStartRating = munStartRating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAccept_customer() {
        return accept_customer;
    }

    public void setAccept_customer(int accept_customer) {
        this.accept_customer = accept_customer;
    }

    public int getPay_customer() {
        return pay_customer;
    }

    public void setPay_customer(int pay_customer) {
        this.pay_customer = pay_customer;
    }

    public int getRating_customer() {
        return rating_customer;
    }

    public void setRating_customer(int rating_customer) {
        this.rating_customer = rating_customer;
    }

    public String getId_offer() {
        return id_offer;
    }

    public void setId_offer(String id_offer) {
        this.id_offer = id_offer;
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

    public String getPhone_customer() {
        return phone_customer;
    }

    public void setPhone_customer(String phone_customer) {
        this.phone_customer = phone_customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExperience_provider() {
        return experience_provider;
    }

    public void setExperience_provider(String experience_provider) {
        this.experience_provider = experience_provider;
    }

    public static Comparator<receiverFromProvider> ByPrice = new Comparator<receiverFromProvider>() {
        @Override
        public int compare(receiverFromProvider one, receiverFromProvider two) {
            return - Integer.valueOf((int) one.price).compareTo(Integer.valueOf((int) two.price));
        }
    };

    public static Comparator<receiverFromProvider> ByRating = new Comparator<receiverFromProvider>() {
        @Override
        public int compare(receiverFromProvider one, receiverFromProvider two) {
            return  - Integer.valueOf((int) one.munStartRating).compareTo(Integer.valueOf((int) two.munStartRating));
        }
    };

    public static Comparator<receiverFromProvider> ByExperince = new Comparator<receiverFromProvider>() {
        @Override
        public int compare(receiverFromProvider one, receiverFromProvider two) {
            return - Integer.valueOf(one.experience_provider).compareTo(Integer.valueOf(two.experience_provider));
        }
    };
}
