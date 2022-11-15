package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.facade.UserFacade;
import com.example.springbootpetproject.repository.UserRepository;
import com.example.springbootpetproject.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceI implements com.example.springbootpetproject.service.serviceInterfaces.UserService {
    private final UserRepository userRepository;
    private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenServiceI confirmationTokenServiceI;
    private final UserFacade userFacade;

    @Autowired
    public UserServiceI(UserRepository userRepository, CustomBCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenServiceI confirmationTokenServiceI, UserFacade userFacade) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenServiceI = confirmationTokenServiceI;
        this.userFacade = userFacade;
    }

    @Override
    @Transactional
    public String addUser(User user) {
        log.debug("In the findByToken method");
        String encodePassword = bCryptPasswordEncoder.passwordEncoder().encode(user.getPassword());

        user.setPassword(encodePassword);

        user.setUserRole(UserRole.USER);

        userRepository.save(user);
        log.info("user was saved");
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenServiceI.saveToken(confirmationToken);
        log.debug("End of findAllCity method");
        return token;
    }


    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        log.debug("In the deleteUserById method");
        if(userRepository.existsUserById(id)){
            userRepository.deleteById(id);
            log.info("user with {} id was deleted",id);
            return true;
        }
        log.warn("user with {} id not found", id);
        log.debug("End of deleteUserById method");
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<User> userPage = userRepository.findAll(changePageable);
        Page<UserDTO> userDTOPage = userPage.map(userFacade::convertUserToUserDTO);
        return userDTOPage;
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

    @Override
    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void topUpAccount(BigDecimal money,String userName) {
        if(money.compareTo(BigDecimal.valueOf(0))<=0){
            throw new IllegalArgumentException("Гроші не можуть бути відємними");
        }
        userRepository.topUpAccount(money,userName);
    }

    @Override
    @Transactional
    public void spendMoney(BigDecimal money,String userName) {
        userRepository.spendMoney(money,userName);
    }

    @Override
    @Transactional
    public void setUserVerification(String username) {
        boolean oldAccountVerificationStatus = userRepository.findByUsername(username).isAccountVerified();
        userRepository.setUserVerification(!oldAccountVerificationStatus,username);
    }

    @Override
    @Transactional
    public Map<String, String> changePassword(String oldPassword, String newPassword, String confirmedNewPassword, String username) {
        Map<String,String> errorsMap = new HashMap<>();
        User user = findUserByUsername(username);
        String activePassword = user.getPassword();
        String encodePassword = bCryptPasswordEncoder.passwordEncoder().encode(confirmedNewPassword);
        if(!bCryptPasswordEncoder.passwordEncoder().matches(oldPassword,activePassword)){
            errorsMap.put("activePasswordNotEquals","Wrong password!!!");
        }
        if(!Validator.isPasswordLengthValid(newPassword)){
            errorsMap.put("firstPasswordError","length must be from 8 to 64 characters");
        }
        if(!Validator.isPasswordLengthValid(confirmedNewPassword)){
            errorsMap.put("secondPasswordError","length must be from 8 to 64 characters");
        }
        if(!Validator.isTheSamePassword(newPassword,confirmedNewPassword)){
            errorsMap.put("differentPasswords","different passwords");
        }
        if(!errorsMap.isEmpty()){
            return errorsMap;
        }

        userRepository.changeUserPassword(encodePassword,username);

        return errorsMap;
    }

}
