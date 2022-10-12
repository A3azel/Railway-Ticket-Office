package com.example.springbootpetproject.customExceptions.registrationExeptions;

public class UserAlreadyExist extends Throwable{
    public UserAlreadyExist() {
    }

    public UserAlreadyExist(String message) {
        super(message);
    }
}
