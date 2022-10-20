package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.customExceptions.registrationExeptions.DifferentPasswords;
import com.example.springbootpetproject.customExceptions.registrationExeptions.UserAlreadyExist;
import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.service.serviceImplementation.ConfirmationTokenService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import com.example.springbootpetproject.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String,String> sendRegistrationConfirmationEmail(User user, String secondPassword, String userGender) {
        Map<String,String> errorsMap = new HashMap<>();
        if(userService.existsUserByUsername(user.getUsername())){
            errorsMap.put("userAlreadyExists","the user with selected name already exists");
        }
        if(userService.existsUserByUserEmail(user.getUserEmail())){
            errorsMap.put("emailAlreadyExists","the email with selected name already exists");
        }
        if(!Validator.isTheSamePassword(user.getPassword(), secondPassword)){
            errorsMap.put("differentPasswords","different passwords");
        }
        if(!errorsMap.isEmpty()){
            return errorsMap;
        }

        user.setUserRole(UserRole.USER);
        user.setUserGender(UserGender.valueOf(userGender));

        /*String token = userService.addUser(user);
        String link = "http://localhost:8080/registration/activate?token=" + token;
        mailService.sendSimpleMessage(
                user.getUserEmail(),
                "Registration",
                link
        );*/
        return errorsMap;
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
