package com.example.springbootpetproject.customExceptions.userExceptions;

public class InvalidCountOfMoney extends Throwable{

    public InvalidCountOfMoney() {
    }

    public InvalidCountOfMoney(String message) {
        super(message);
    }
}
