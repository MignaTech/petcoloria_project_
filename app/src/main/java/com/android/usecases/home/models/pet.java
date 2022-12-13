package com.android.usecases.home.models;

public class pet {

    private String name;
    private String specie;
    private String age;
    private String color;
    private String weight;
    private String last_vaccine;
    private String sex;
    private String breed;
    private String id_user;

    public String getId_pet() {
        return id_pet;
    }

    public void setId_pet(String id_pet) {
        this.id_pet = id_pet;
    }

    private String id_pet;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLast_vaccine() {
        return last_vaccine;
    }

    public void setLast_vaccine(String last_vaccine) {
        this.last_vaccine = last_vaccine;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return name;
    }
}
