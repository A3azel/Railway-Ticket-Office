package com.example.springbootpetproject.customExceptions.userExceptions;

public class InvalidCountOfMoney extends RuntimeException{

    public InvalidCountOfMoney() {
    }

    public InvalidCountOfMoney(String message) {
        super(message);
    }
}
