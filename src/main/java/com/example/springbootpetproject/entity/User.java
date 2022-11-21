package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
//@Builder
@NoArgsConstructor
@Table(name = "user_info")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Pattern(regexp = "\\w{4,64}")
    @Size(min = 4, max = 64, message = "length must be from 4 to 64 characters")
    @Column(name = "username")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁёІіЇїЄє]{1,40}$")
    @Size(min = 1, max = 64, message = "length must be from 1 to 64 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁёІіЇїЄє]{1,40}$")
    @Size(min = 1, max = 64, message = "length must be from 1 to 64 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(min = 8, max = 64, message = "length must be from 8 to 64 characters")
    @Column(name = "user_password")
    private String password;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Column(name = "user_count_of_money")
    private BigDecimal userCountOfMoney;

    @Column(name = "acount_verified")
    private boolean accountVerified;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_gender_id")
    private UserGender userGender;

    //@Pattern(regexp = "\\(?\\+?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})? ?(\\w{1,10}\\s?\\d{1,6})?")
    @Column(name = "user_phone")
    private String userPhone;

    @Pattern(regexp = "^([A-Za-z0-9_-]+\\.)*[A-Za-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")
    @Column(name = "user_email")
    @NotNull
    @Email
    private String userEmail;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Order> ordersSet;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UserComment> userCommentsSet;

    @OneToMany(mappedBy = "user")
    private Set<ConfirmationToken> confirmationTokens;

}
