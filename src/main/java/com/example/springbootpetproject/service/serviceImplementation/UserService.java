package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.configurtion.CustomBCryptPasswordEncoder;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.ConfirmationToken;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.UserRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserServiceInterface;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public String addUser(User user) {
        if(existsUserByUsername(user.getUsername())){
            throw new IllegalArgumentException("Login вже використовується");
        }
        if(existsUserByUserEmail(user.getUserEmail())){
            throw new IllegalArgumentException("Email вже використовується");
        }
        if(Validator.isEmail(user.getUserEmail())){
            throw new IllegalArgumentException("Не коректний Email");
        }
        if(Validator.isNameOrSurname(user.getFirstName())){
            throw new IllegalArgumentException("Не коректне імя");
        }
        if(Validator.isNameOrSurname(user.getLastName())){
            throw new IllegalArgumentException("Не коректне прізвище");
        }

        String encodePassword = bCryptPasswordEncoder.passwordEncoder().encode(user.getPassword());

        user.setPassword(encodePassword);

        user.setUserRole(UserRole.USER);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveToken(confirmationToken);

        return token;
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
    public Page<User> getAllUsers(Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userRepository.findAll(changePageable);
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
    public UserDTO convertUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDTO.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserCountOfMoney(user.getUserCountOfMoney());
        userDTO.setAccountVerified(user.isAccountVerified());
        userDTO.setUserRole(user.getUserRole().name());
        userDTO.setUserGender(user.getUserGender().name());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserPhone(user.getUserPhone());
        return userDTO;
    }


}
