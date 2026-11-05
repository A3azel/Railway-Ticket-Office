package com.example.springbootpetproject.customExceptions.orderExceptions;

public class OrderNotFound extends RuntimeException{
    public OrderNotFound() {
        super();
    }

    public OrderNotFound(String message) {
        super(message);
    }
}
