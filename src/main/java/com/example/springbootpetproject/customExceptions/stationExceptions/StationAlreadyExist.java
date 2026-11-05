package com.example.springbootpetproject.customExceptions.stationExceptions;

public class StationAlreadyExist extends RuntimeException{
    public StationAlreadyExist() {
        super();
    }

    public StationAlreadyExist(String message) {
        super(message);
    }
}
