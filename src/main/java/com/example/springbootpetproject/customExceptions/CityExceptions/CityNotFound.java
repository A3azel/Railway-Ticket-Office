package com.example.springbootpetproject.customExceptions.CityExceptions;

public class CityNotFound extends Throwable{
    public CityNotFound() {
        super();
    }

    public CityNotFound(String message) {
        super(message);
    }
}
