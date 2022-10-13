package com.example.springbootpetproject.customExceptions.routeExceptions;

public class RouteNotFound extends Throwable{
    public RouteNotFound() {
        super();
    }

    public RouteNotFound(String message) {
        super(message);
    }
}
