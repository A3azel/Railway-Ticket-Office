package com.example.springbootpetproject.customExceptions.routeExceptions;

public class DataCompareError extends RuntimeException{
    public DataCompareError() {
        super();
    }

    public DataCompareError(String message) {
        super(message);
    }
}
