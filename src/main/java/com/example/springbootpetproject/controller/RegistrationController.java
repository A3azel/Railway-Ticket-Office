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

        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        //String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        String submitPassword = request.getParameter("submitPassword");
        if(!Validator.isTheSamePassword(password,submitPassword)){
            throw new IllegalArgumentException("different passwords");
        }
        User user = new User(username,firstName,lastName,password, UserGender.MAN,email);
        if(!phone.equals("")){
            user.setUserPhone(phone);
        }
        registrationService.sendRegistrationConfirmationEmail(user);
        return "redirect:/login";
    }

    @GetMapping("/activate/{token}")
    public String activateAccount(@PathVariable("token") String tokenCode){
        registrationService.confirmToken(tokenCode);
        return "/login";
    }
}
