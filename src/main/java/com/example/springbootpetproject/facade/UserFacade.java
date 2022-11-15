package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserFacade {

    public UserDTO convertUserToUserDTO(User user){
        log.debug("In the convertUserToUserDTO method");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserCountOfMoney(user.getUserCountOfMoney());
        userDTO.setAccountVerified(user.isAccountVerified());
        userDTO.setUserRole(user.getUserRole().name());
        userDTO.setUserGender(user.getUserGender().name());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserPhone(user.getUserPhone());
        log.info("UserDTO converted user: {}", userDTO);
        log.debug("End of convertUserToUserDTO method");
        return userDTO;
    }
}
