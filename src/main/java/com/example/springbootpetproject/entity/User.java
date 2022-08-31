package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class User implements Serializable/*, UserDetails*/ {
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

    @Column(name = "user_count_of_money")
    private BigDecimal userCountOfMoney;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    @NotNull
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_gender_id")
    @NotNull
    private UserGender userGender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

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

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Orders> ordersSet;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UserComments> userCommentsSet;

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

    @Override
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
    }

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(UserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }*/
}
