package com.example.springbootpetproject.customExceptions.cityExceptions;

public class CityAlreadyExist extends RuntimeException{

    public CityAlreadyExist() {
    }

    public CityAlreadyExist(String message){
        super(message);
    }
}
