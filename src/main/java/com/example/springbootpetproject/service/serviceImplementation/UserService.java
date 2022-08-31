package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.UserRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserServiceInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserService(UserRepository userRepository, CustomBCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        if(existsUserByUsername(user.getUsername())){
            throw new IllegalArgumentException();
        }
        if(existsUserByUserEmail(user.getUserEmail())){
            throw new IllegalArgumentException();
        }
        if(Validator.isEmail(user.getUserEmail())){

        }
        if(Validator.isNameOrSurname(user.getFirstName())){

        }
        if(Validator.isNameOrSurname(user.getLastName())){

        }

        String encodePassword = bCryptPasswordEncoder.passwordEncoder().encode(user.getPassword());

        user.setPassword(encodePassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveToken(confirmationToken);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        if(userRepository.existsUserById(id)){
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean existsUserByUserEmail(String email) {
        return userRepository.existsUserByUserEmail(email);
    }

    @Override
    @Transactional
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }
}
