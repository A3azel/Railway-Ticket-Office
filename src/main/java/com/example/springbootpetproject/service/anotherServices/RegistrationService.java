package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserGender;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.service.serviceImplementation.ConfirmationTokenServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import com.example.springbootpetproject.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RegistrationService {

    private final UserServiceI userServiceI;
    private final ConfirmationTokenServiceI confirmationTokenServiceI;
    private final MailService mailService;

    @Autowired
    public RegistrationService(UserServiceI userServiceI, ConfirmationTokenServiceI confirmationTokenServiceI, MailService mailService) {
        this.userServiceI = userServiceI;
        this.confirmationTokenServiceI = confirmationTokenServiceI;
        this.mailService = mailService;
    }


    @Transactional
    public Map<String,String> sendRegistrationConfirmationEmail(User user, String secondPassword, String userGender) {
        log.debug("In the sendRegistrationConfirmationEmail method");
        Map<String,String> errorsMap = new HashMap<>();
        if(!user.getUserPhone().equals("") && Validator.isPhoneValid(user.getUserPhone())){
            errorsMap.put("phoneNumberError","invalid phone number");
            return errorsMap;
        }
        if(userServiceI.existsUserByUsername(user.getUsername())){
            log.warn("the user with {} name already exist",user.getUsername());
            errorsMap.put("userAlreadyExists","user with selected name already exists");
        }
        if(userServiceI.existsUserByUserEmail(user.getUserEmail())){
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
        user.setUserCountOfMoney(BigDecimal.ZERO);
        user.setUserGender(UserGender.valueOf(userGender));
        log.info("Trying to add a user");
        String token = userServiceI.addUser(user);
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
        ConfirmationToken confirmationToken = confirmationTokenServiceI.findByToken(token);

        if(confirmationToken == null){
            log.warn("token {} not found",token);
            throw new IllegalStateException("token not found");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.info("token {} is expired",token);
            userServiceI.deleteUserById(confirmationToken.getUser().getId());
            confirmationTokenServiceI.deleteToken(token);
        }
        //confirmationTokenService.updateConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setAccountVerified(true);
        log.info("Saving user {}", user.getUsername());
        userServiceI.save(user);
        confirmationTokenServiceI.deleteToken(token);
        log.debug("End of confirmToken method");

    }



}
