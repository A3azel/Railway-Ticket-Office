package com.example.springbootpetproject.customExceptions.routeExceptions;

public class RouteNotFound extends RuntimeException{
    public RouteNotFound() {
        super();
    }

    public RouteNotFound(String message) {
        super(message);
    }
}
