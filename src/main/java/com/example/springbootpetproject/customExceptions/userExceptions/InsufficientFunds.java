package com.example.springbootpetproject.customExceptions.userExceptions;

public class InsufficientFunds extends RuntimeException{
    public InsufficientFunds() {
        super();
    }

    public InsufficientFunds(String message) {
        super(message);
    }
}
