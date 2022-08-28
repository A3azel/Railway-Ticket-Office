package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.UserComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentsRepository extends JpaRepository<UserComments,Long> {
}
