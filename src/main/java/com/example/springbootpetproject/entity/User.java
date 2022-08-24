package com.example.springbootpetproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @Column(name = "username")
    @NotEmpty
    @NotNull
    private String username;

    @Column(name = "first_name")
    @NotEmpty
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @NotNull
    private String lastName;

    @Column(name = "user_password")
    @NotEmpty
    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    @NotNull
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_gender_id")
    @NotNull
    private UserGender userGender;

    @Column(name = "user_phone")
    @NotEmpty
    @NotNull
    @UniqueElements
    private String userPhone;

    @Column(name = "user_email")
    @NotEmpty
    @NotNull
    @UniqueElements
    @Email
    private String userEmail;
}
