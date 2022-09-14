package com.example.springbootpetproject.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private BigDecimal userCountOfMoney;
    private boolean accountVerified;
    private String userRole;
    private String userGender;
    private String userPhone;
    private String userEmail;

}
