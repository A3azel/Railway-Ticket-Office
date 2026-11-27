package com.example.springbootpetproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.springbootpetproject.controller.Paths.LOGIN_FILE;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String returnLoginPage(){
        return LOGIN_FILE;
    }


    @GetMapping("/error")
    public String loginError(Model model){
        String errorMessage = "Invalid username or password";
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
}
