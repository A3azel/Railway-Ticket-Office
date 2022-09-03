package com.example.springbootpetproject.validator;

import java.util.regex.Pattern;

public class Validator {
    private static final String NAME_REGULAR_EXPRESSION = "^[A-Z\\p{IsCyrillic}][A-Za-z\\p{IsCyrillic}]{1,40}$";
    private static final String PASSWORD_REGULAR_EXPRESSION = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";
    private static final String EMAIL_REGULAR_EXPRESSION = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";


    public static boolean isEmail(String email){
        return Pattern.matches(EMAIL_REGULAR_EXPRESSION, email);
    }

    public static boolean isNameOrSurname(String name){
        return Pattern.matches(NAME_REGULAR_EXPRESSION, name);
    }

    public static boolean isTheSamePassword(String password, String submitPassword){
        return password.equals(submitPassword);
    }

}
