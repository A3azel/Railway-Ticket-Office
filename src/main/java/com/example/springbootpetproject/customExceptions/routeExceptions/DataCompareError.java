package com.example.springbootpetproject.customExceptions.routeExceptions;

public class DataCompareError extends Throwable{
    public DataCompareError() {
        super();
    }

    public DataCompareError(String message) {
        super(message);
    }
}
