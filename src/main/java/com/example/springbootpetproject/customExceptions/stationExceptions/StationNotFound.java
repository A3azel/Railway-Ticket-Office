package com.example.springbootpetproject.customExceptions.stationExceptions;

public class StationNotFound extends RuntimeException{
    public StationNotFound() {
        super();
    }

    public StationNotFound(String message) {
        super(message);
    }
}
