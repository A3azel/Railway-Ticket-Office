package com.example.springbootpetproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
//@Builder
@NoArgsConstructor
@Table(name = "user_info")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty
    private String lastName;

    @Column(name = "user_password", nullable = false)
    @NotEmpty
    @Size(min = 8, max = 64)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id", nullable = false)
 //   @Builder.Default
    private UserRole userRole /*= UserRole.User*/;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_gender_id")
    private UserGender userGender;

    @Column(name = "user_phone", unique = true)
//    @Builder.Default
    private String userPhone/* = null*/;

    @Column(name = "user_email", unique = true)
    @Email
//    @Builder.Default
    private String userEmail /*= null*/;
}
