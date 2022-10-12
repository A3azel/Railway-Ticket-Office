package com.example.springbootpetproject.customExceptions.StationExceptions;

public class StationAlreadyExist extends Throwable{
    public StationAlreadyExist() {
        super();
    }

    public StationAlreadyExist(String message) {
        super(message);
    }
}
