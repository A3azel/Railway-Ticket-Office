package com.example.springbootpetproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/country")
public class TestController {

    @GetMapping("/all")
    public String greeting() {

        return "index";
    }

}
