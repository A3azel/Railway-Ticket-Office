package com.example.springbootpetproject.customExceptions.cityExceptions;

public class InvalidNameOfCity extends RuntimeException{

    public InvalidNameOfCity() {

    }

    public InvalidNameOfCity(String message) {
        super(message);
    }

}
