package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.registrationExeptions.DifferentPasswords;
import com.example.springbootpetproject.customExceptions.registrationExeptions.UserAlreadyExist;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.service.anotherServices.RegistrationService;
import com.example.springbootpetproject.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String toRegistration(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String makeAnRegistration(@ModelAttribute @Valid User user, Errors errors, Model model,
                                     @RequestParam ("secondPassword")  String secondPassword){
        if(errors.hasErrors()){
            return "registration";
        }
        try{
            registrationService.sendRegistrationConfirmationEmail(user,secondPassword);
        } catch (UserAlreadyExist userAlreadyExist) {
            model.addAttribute("userAlreadyExist", userAlreadyExist.getMessage());
            return "registration";
        } catch (DifferentPasswords differentPasswords) {
            model.addAttribute("differentPasswords", differentPasswords.getMessage());
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{token}")
    public String activateAccount(@PathVariable("token") String tokenCode){
        registrationService.confirmToken(tokenCode);
        return "/login";
    }
}
