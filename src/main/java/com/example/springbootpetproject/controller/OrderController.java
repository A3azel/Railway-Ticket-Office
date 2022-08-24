package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final UserService userService;

    @Autowired
    public OrderController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping
    public String test(Model model){
        Train train = (Train) model.getAttribute("selectedTrain");
        model.addAttribute("selectedTrain",train);
        return "trainInfo";
    }*/
    @GetMapping
    public String test(Model model){
        Train train = (Train) model.getAttribute("selectedTrain");
        model.addAttribute("selectedTrain",train);
        return "trainInfo";
    }
}
