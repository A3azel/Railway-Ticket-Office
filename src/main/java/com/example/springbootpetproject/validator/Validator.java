package com.example.springbootpetproject.validator;

public class Validator {
    private static final String NAME_REGULAR_EXPRESSION = "^[A-Z\\p{IsCyrillic}][A-Za-z\\p{IsCyrillic}]{1,40}$";
    private static final String PASSWORD_REGULAR_EXPRESSION = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";

}
