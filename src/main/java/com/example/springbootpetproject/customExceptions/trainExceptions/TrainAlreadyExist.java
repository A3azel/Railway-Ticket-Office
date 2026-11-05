package com.example.springbootpetproject.customExceptions.trainExceptions;

public class TrainAlreadyExist extends RuntimeException{

    public TrainAlreadyExist() {
    }

    public TrainAlreadyExist(String message) {
        super(message);
    }
}
