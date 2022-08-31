package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.service.serviceImplementation.ConfirmationTokenService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;
    private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailService mailService;

    @Autowired
    public RegistrationService(UserService userService, CustomBCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService, MailService mailService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.mailService = mailService;
    }



}
