package com.example.springbootpetproject.customExceptions.trainExceptions;

public class InvalidCountOfSeats extends Throwable{

    public InvalidCountOfSeats() {

    }

    public InvalidCountOfSeats(String message) {
        super(message);
    }
}
