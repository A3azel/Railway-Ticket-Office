package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentsRepository extends JpaRepository<UserComments,Long> {

    Page<UserComments> findAllByTrain_TrainNumber(String trainNumber, Pageable pageable);

    UserComments findByUserUsernameAndTrainTrainNumber(String username, String trainNumber);

    List<UserComments> findByTrain_TrainNumber(String trainNumber);

    Page<UserComments> findAllByUserUsername(String userName, Pageable pageable);
}
