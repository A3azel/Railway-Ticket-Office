package com.example.springbootpetproject.customExceptions.userExceptions;

public class InsufficientFunds extends Throwable{
    public InsufficientFunds() {
        super();
    }

    public InsufficientFunds(String message) {
        super(message);
    }
}
