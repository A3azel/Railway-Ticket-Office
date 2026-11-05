package com.example.springbootpetproject.customExceptions.routeExceptions;

public class ProblemWithSeatsCount extends RuntimeException{
    public ProblemWithSeatsCount() {
        super();
    }

    public ProblemWithSeatsCount(String message) {
        super(message);
    }
}
