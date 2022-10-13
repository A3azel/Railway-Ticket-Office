package com.example.springbootpetproject.customExceptions.stationExceptions;

public class StationAlreadyExist extends Throwable{
    public StationAlreadyExist() {
        super();
    }

    public StationAlreadyExist(String message) {
        super(message);
    }
}
