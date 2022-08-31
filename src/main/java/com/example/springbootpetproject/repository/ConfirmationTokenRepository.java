package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByToken(String token);

    @Modifying
    @Query(value = "UPDATE confirmation_token SET confirmed_at = :updateTime WHERE token = :token", nativeQuery = true)
    int updateConfirmedAt(@Param("token") String token
    , @Param("updateTime")LocalDateTime updateTime);

}
