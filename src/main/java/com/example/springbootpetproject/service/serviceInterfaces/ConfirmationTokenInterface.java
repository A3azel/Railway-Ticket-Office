package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.ConfirmationToken;

public interface ConfirmationTokenInterface {

    ConfirmationToken findByToken(String token);

    void saveToken(ConfirmationToken token);

    int updateConfirmedAt(String token);

    void deleteToken(String token);
}
