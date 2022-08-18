package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.User;

import java.util.List;

public interface UserServiceInterface {

    boolean addUser(User user);

    boolean deleteUserById(Long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    User getUserByEmailAndPassword(String email, String password);


}
