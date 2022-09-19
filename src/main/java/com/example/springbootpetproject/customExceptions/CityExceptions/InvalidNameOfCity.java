package com.example.springbootpetproject.customExceptions.CityExceptions;

public class InvalidNameOfCity extends Throwable{

    public InvalidNameOfCity() {

    }

    public InvalidNameOfCity(String message) {
        super(message);
    }

}
