package com.example.springbootpetproject.entity;

public enum TrainPlace {
    S(""), COMPARTMENT("Купе"), SUITE("Люкс");

    private final String place;

    TrainPlace(String place) {
        this.place = place;
    }

    public String getPlace(){
        return place;
    }
}
