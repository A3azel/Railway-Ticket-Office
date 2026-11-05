package com.example.springbootpetproject.customExceptions.cityExceptions;

public class CityNotFound extends RuntimeException{
    public CityNotFound() {
        super();
    }

    public CityNotFound(String message) {
        super(message);
    }
}
