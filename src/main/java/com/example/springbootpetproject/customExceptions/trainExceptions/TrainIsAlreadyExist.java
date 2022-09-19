package com.example.springbootpetproject.customExceptions.trainExceptions;

public class TrainIsAlreadyExist extends Throwable{

    public TrainIsAlreadyExist() {
    }

    public TrainIsAlreadyExist(String message) {
        super(message);
    }
}
