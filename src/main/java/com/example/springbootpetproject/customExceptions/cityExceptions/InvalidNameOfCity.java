package com.example.springbootpetproject.customExceptions.cityExceptions;

public class InvalidNameOfCity extends Throwable{

    public InvalidNameOfCity() {

    }

    public InvalidNameOfCity(String message) {
        super(message);
    }

}
