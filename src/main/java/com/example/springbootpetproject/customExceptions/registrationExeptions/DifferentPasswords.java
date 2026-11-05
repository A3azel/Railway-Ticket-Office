package com.example.springbootpetproject.customExceptions.registrationExeptions;

public class DifferentPasswords extends RuntimeException{
    public DifferentPasswords() {
    }

    public DifferentPasswords(String message) {
        super(message);
    }
}
