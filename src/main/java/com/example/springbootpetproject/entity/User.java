package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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
    @Size(min = 4, max = 64, message = "length must be from 4 to 64 characters")
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(min = 1, max = 64, message = "length must be from 1 to 64 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 64, message = "length must be from 1 to 64 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(min = 8, max = 64, message = "length must be from 1 to 64 characters")
    @Column(name = "user_password")
    private String password;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Column(name = "user_count_of_money")
    private BigDecimal userCountOfMoney;

    @Column(name = "acount_verified")
    private boolean accountVerified;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    @NotNull
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_gender_id")
    @NotNull
    private UserGender userGender;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_email")
    @NotEmpty
    @Email
    private String userEmail;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Orders> ordersSet;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UserComments> userCommentsSet;

    @OneToMany(mappedBy = "user")
    private Set<ConfirmationToken> confirmationTokens;

    public User(String username, String firstName, String lastName, String password, UserGender userGender, String userEmail) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userGender = userGender;
        this.userEmail = userEmail;
    }

    public User(String username, String firstName, String lastName, String password, UserGender userGender, String userPhone, String userEmail) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && Objects.equals(userCountOfMoney, user.userCountOfMoney) && userRole == user.userRole && userGender == user.userGender && Objects.equals(userPhone, user.userPhone) && Objects.equals(userEmail, user.userEmail) && Objects.equals(userCommentsSet, user.userCommentsSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, password, userCountOfMoney, userRole, userGender, userPhone, userEmail);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", userCountOfMoney=" + userCountOfMoney +
                ", userRole=" + userRole +
                ", userGender=" + userGender +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }*/

}
