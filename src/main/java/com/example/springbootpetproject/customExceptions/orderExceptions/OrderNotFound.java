package com.example.springbootpetproject.customExceptions.orderExceptions;

public class OrderNotFound extends Throwable{
    public OrderNotFound() {
        super();
    }

    public OrderNotFound(String message) {
        super(message);
    }
}
