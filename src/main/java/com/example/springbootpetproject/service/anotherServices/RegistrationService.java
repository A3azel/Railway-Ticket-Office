package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.service.serviceImplementation.ConfirmationTokenService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailService mailService;

    @Autowired
    public RegistrationService(UserService userService, ConfirmationTokenService confirmationTokenService, MailService mailService) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.mailService = mailService;
    }


    @Transactional
    public void sendRegistrationConfirmationEmail(User user){
        String token = userService.addUser(user);
        String link = "http://localhost:8080/registration/activate?token=" + token;
        mailService.sendSimpleMessage(
                user.getUserEmail(),
                "Registration",
                link
        );
    }

    @Transactional
    public void confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);

        if(confirmationToken == null){
            throw new IllegalStateException("token not found");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        //confirmationTokenService.updateConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setAccountVerified(true);
        userService.save(user);
        confirmationTokenService.deleteToken(token);

    }



}
