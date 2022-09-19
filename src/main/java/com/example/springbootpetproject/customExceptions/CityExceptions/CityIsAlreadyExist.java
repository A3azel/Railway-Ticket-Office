package com.example.springbootpetproject.customExceptions.CityExceptions;

public class CityIsAlreadyExist extends Throwable{

    public CityIsAlreadyExist() {
    }

    public CityIsAlreadyExist(String message){
        super(message);
    }
}
