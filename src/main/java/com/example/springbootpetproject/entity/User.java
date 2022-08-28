package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
//@Builder
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

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Orders> ordersSet;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UserComments> userCommentsSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && userRole == user.userRole && userGender == user.userGender && Objects.equals(userPhone, user.userPhone) && Objects.equals(userEmail, user.userEmail) && Objects.equals(userCommentsSet, user.userCommentsSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, password, userRole, userGender, userPhone, userEmail, userCommentsSet);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
                ", userGender=" + userGender +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userCommentsSet=" + userCommentsSet +
                '}';
    }
}
