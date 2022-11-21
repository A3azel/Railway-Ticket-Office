package com.example.springbootpetproject.controller;

import  com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.service.anotherServices.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping
    public String makeAnRegistration(@ModelAttribute @Valid User user,@RequestParam("radioName") String userGender, Errors errors
            , Model model, @RequestParam ("secondPassword")  String secondPassword){
        user.setUserGender(UserGender.valueOf(userGender));
        if(errors.hasErrors()){
            return "registration";
        }

        Map<String,String> errorsMap = registrationService.sendRegistrationConfirmationEmail(user, secondPassword, userGender);
        if(!errorsMap.isEmpty()){
            model.mergeAttributes(errorsMap);
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("token") String tokenCode){
        registrationService.confirmToken(tokenCode);
        return "/login";
    }
}
