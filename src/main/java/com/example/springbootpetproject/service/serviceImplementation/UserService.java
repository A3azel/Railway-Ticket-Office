package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.UserRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserServiceInterface;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        if(userRepository.existsUserByUserEmail(user.getUserEmail())){
            return false;
        }
        userRepository.save(user);
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
    @Transactional(readOnly = true)
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.getUserByUserEmailAndPassword(email, password);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
