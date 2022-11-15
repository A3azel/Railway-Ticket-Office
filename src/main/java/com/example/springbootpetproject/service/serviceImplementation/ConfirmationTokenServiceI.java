package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.repository.ConfirmationTokenRepository;
import com.example.springbootpetproject.service.serviceInterfaces.ConfirmationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ConfirmationTokenServiceI implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceI(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ConfirmationToken findByToken(String token) {
        log.debug("In the findByToken method");
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        log.info("Find confirmationToken {}", confirmationToken.getToken());
        log.debug("End of findAllCity method");
        return confirmationToken;
    }

    @Override
    @Transactional
    public void saveToken(ConfirmationToken token) {
        log.debug("In the saveToken method");
        confirmationTokenRepository.save(token);
    }

    @Override
    @Transactional
    public int updateConfirmedAt(String token) {
        log.debug("In the updateConfirmedAt method");
        return confirmationTokenRepository.updateConfirmedAt(token,LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteToken(String token){
        log.debug("In the deleteToken method");
        confirmationTokenRepository.deleteByToken(token);
        log.info("Token {} deleted", token);
        log.debug("End of findAllCity method");
    }
}
