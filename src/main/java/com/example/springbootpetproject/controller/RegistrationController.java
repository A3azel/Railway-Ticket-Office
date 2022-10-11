package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.service.anotherServices.RegistrationService;
import com.example.springbootpetproject.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String toRegistration(){
        return "registration";
    }

    @PostMapping
    public String makeAnRegistration(HttpServletRequest request){
        registrationService.sendRegistrationConfirmationEmail(request);
        return "redirect:/login";
    }

    @GetMapping("/activate/{token}")
    public String activateAccount(@PathVariable("token") String tokenCode){
        registrationService.confirmToken(tokenCode);
        return "/login";
    }
}
