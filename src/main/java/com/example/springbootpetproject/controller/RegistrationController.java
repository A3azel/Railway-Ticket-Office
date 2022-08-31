package com.example.springbootpetproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @GetMapping
    public String toRegistration(Model model){
        return "registration";
    }

    @PostMapping
    public String makeAnRegistration(Model model){
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateAccount(Model model, @PathVariable("code") String tokenCode){
        return "/login";
    }
}
