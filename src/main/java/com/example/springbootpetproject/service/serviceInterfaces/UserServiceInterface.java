package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface UserServiceInterface {

    String addUser(User user);

    boolean deleteUserById(Long id);

    User getUserById(Long id);

    Page<User> getAllUsers(Pageable pageable, int pageNumber);

    User findUserByUsername(String username);

    boolean existsUserByUserEmail(String email);

    boolean existsUserByUsername(String username);

    void save(User user);

    void topUpAccount(BigDecimal money,String userName);

    void spendMoney(BigDecimal money,String userName);

    void setUserVerification(String username);

}
