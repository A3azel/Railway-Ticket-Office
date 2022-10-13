package com.example.springbootpetproject.customExceptions.cityExceptions;

public class CityAlreadyExist extends Throwable{

    public CityAlreadyExist() {
    }

    public CityAlreadyExist(String message){
        super(message);
    }
}
