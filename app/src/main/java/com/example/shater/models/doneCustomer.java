package com.example.shater.models;

public class doneCustomer {

    boolean insertedPayment ;
    boolean insertedRating;
    String date ;

    public doneCustomer() {
    }

    public boolean isInsertedPayment() {
        return insertedPayment;
    }

    public void setInsertedPayment(boolean insertedPayment) {
        this.insertedPayment = insertedPayment;
    }

    public boolean isInsertedRating() {
        return insertedRating;
    }

    public void setInsertedRating(boolean insertedRating) {
        this.insertedRating = insertedRating;
    }
}
