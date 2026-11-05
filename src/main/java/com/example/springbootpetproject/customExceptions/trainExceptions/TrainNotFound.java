package com.example.springbootpetproject.customExceptions.trainExceptions;

public class TrainNotFound extends RuntimeException{
    public TrainNotFound() {
        super();
    }

    public TrainNotFound(String message) {
        super(message);
    }
}
