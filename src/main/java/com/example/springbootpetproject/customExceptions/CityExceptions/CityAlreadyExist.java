package com.example.springbootpetproject.customExceptions.CityExceptions;

public class CityAlreadyExist extends Throwable{

    public CityAlreadyExist() {
    }

    public CityAlreadyExist(String message){
        super(message);
    }
}
