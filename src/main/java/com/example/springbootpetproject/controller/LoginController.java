package com.example.springbootpetproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.springbootpetproject.controller.Paths.LOGIN_FILE;

@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String returnLoginPage(){
        return LOGIN_FILE;
    }
}
