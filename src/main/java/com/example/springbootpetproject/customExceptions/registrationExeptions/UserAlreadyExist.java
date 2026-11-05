package com.example.springbootpetproject.customExceptions.registrationExeptions;

public class UserAlreadyExist extends RuntimeException{
    public UserAlreadyExist() {
    }

    public UserAlreadyExist(String message) {
        super(message);
    }
}
