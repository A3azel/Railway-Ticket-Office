package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.ConfirmationToken;

import java.time.LocalDateTime;

public interface ConfirmationTokenInterface {

    ConfirmationToken findByToken(String token);

    void saveToken(ConfirmationToken token);

    int updateConfirmedAt(String token);
}
