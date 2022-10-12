package com.example.springbootpetproject.customExceptions.trainExceptions;

public class TrainAlreadyExist extends Throwable{

    public TrainAlreadyExist() {
    }

    public TrainAlreadyExist(String message) {
        super(message);
    }
}
