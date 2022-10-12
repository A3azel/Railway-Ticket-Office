package com.example.springbootpetproject.customExceptions.registrationExeptions;

public class DifferentPasswords extends Throwable{
    public DifferentPasswords() {
    }

    public DifferentPasswords(String message) {
        super(message);
    }
}
