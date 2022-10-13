package com.example.springbootpetproject.customExceptions.userExceptions;

public class UserNotFound extends Throwable{
    public UserNotFound() {
        super();
    }

    public UserNotFound(String message) {
        super(message);
    }
}
