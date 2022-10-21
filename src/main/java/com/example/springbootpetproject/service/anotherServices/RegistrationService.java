package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.service.serviceImplementation.ConfirmationTokenService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import com.example.springbootpetproject.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
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
        log.debug("In the sendRegistrationConfirmationEmail method");
        Map<String,String> errorsMap = new HashMap<>();
        if(userService.existsUserByUsername(user.getUsername())){
            log.warn("the user with {} name already exist",user.getUsername());
            errorsMap.put("userAlreadyExists","user with selected name already exists");
        }
        if(userService.existsUserByUserEmail(user.getUserEmail())){
            log.warn("email with {} name already exists",user.getUserEmail() );
            errorsMap.put("emailAlreadyExists","email with selected name already exists");
        }
        if(!Validator.isTheSamePassword(user.getPassword(), secondPassword)){
            log.warn("different passwords");
            errorsMap.put("differentPasswords","different passwords");
        }
        if(!errorsMap.isEmpty()){
            return errorsMap;
        }

        user.setUserRole(UserRole.USER);
        user.setUserGender(UserGender.valueOf(userGender));
        log.info("Trying to add a user");
        String token = userService.addUser(user);
        String link = "http://localhost:8080/registration/activate?token=" + token;
        mailService.sendSimpleMessage(
                user.getUserEmail(),
                "Registration",
                link
        );
        log.debug("End of sendRegistrationConfirmationEmail method");
        return errorsMap;
    }

    @Transactional
    public void confirmToken(String token){
        log.debug("In the confirmToken method");
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);

        if(confirmationToken == null){
            log.warn("token {} not found",token);
            throw new IllegalStateException("token not found");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.info("token {} is expired",token);
            userService.deleteUserById(confirmationToken.getUser().getId());
            confirmationTokenService.deleteToken(token);
        }
        //confirmationTokenService.updateConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setAccountVerified(true);
        log.info("Saving user {}", user.getUsername());
        userService.save(user);
        confirmationTokenService.deleteToken(token);
        log.debug("End of confirmToken method");

    }



}
