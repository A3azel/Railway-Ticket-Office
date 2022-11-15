package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.UserComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentsRepository extends JpaRepository<UserComment,Long> {

    Page<UserComment> findAllByTrain_TrainNumber(String trainNumber, Pageable pageable);

    Page<UserComment> findAllByTrain_Id(Long od, Pageable pageable);

    UserComment findByUserUsernameAndTrainTrainNumber(String username, String trainNumber);

    List<UserComment> findByTrain_TrainNumber(String trainNumber);

    Page<UserComment> findAllByUserUsername(String userName, Pageable pageable);
}
