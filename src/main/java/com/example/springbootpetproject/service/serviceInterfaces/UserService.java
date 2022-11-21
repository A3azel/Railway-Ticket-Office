package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.userExceptions.InsufficientFunds;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService {

    String addUser(User user);

    boolean deleteUserById(Long id);

    User getUserById(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable, int pageNumber, String direction, String sort);

    User findUserByUsername(String username);

    boolean existsUserByUserEmail(String email);

    boolean existsUserByUsername(String username);

    void save(User user);

    void topUpAccount(BigDecimal money,String userName);

    void spendMoney(BigDecimal money,String userName) throws InsufficientFunds;

    void setUserVerification(String username);

    Map<String,String> changePassword(String oldPassword, String newPassword, String confirmedNewPassword, String username);
}
