package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.repository.ConfirmationTokenRepository;
import com.example.springbootpetproject.service.serviceInterfaces.ConfirmationTokenInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenService implements ConfirmationTokenInterface {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void saveToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    @Transactional
    public int updateConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token,LocalDateTime.now());
    }

    public void deleteToken(String token){
        confirmationTokenRepository.deleteByToken(token);
    }
}
