package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getUserByUserEmailAndPassword(String email,String password);
    User getUserById(Long id);
    boolean existsUserByUserEmail(String email);
    boolean existsUserById(Long id);
    User findByUsername(String username);
    //boolean existsUserByUserIdAndUserRoleIs
}
