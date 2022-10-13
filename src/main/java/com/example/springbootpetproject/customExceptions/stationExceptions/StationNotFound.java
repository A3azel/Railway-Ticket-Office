package com.example.springbootpetproject.customExceptions.stationExceptions;

public class StationNotFound extends Throwable{
    public StationNotFound() {
        super();
    }

    public StationNotFound(String message) {
        super(message);
    }
}
