package com.android.usecases.home.models;

public class date {
    private String id_customer;
    private String id_pet;
    private String id_doctor;
    private String request;
    private String description;

    public String getDateID() {
        return dateID;
    }

    public void setDateID(String dateID) {
        this.dateID = dateID;
    }

    private String dateID;

    public String getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(String id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getId_pet() {
        return id_pet;
    }

    public void setId_pet(String id_pet) {
        this.id_pet = id_pet;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
