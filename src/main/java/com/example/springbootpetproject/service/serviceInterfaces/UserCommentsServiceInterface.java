package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCommentsServiceInterface {

    void addComment(UserComments userComments);

    void setComment(UserComments userComments);

    void deleteComment(Long id);

    Page<UserComments> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber);

    UserComments findByUserNameAndTrainNumber(String username, String trainNumber);

    List<UserComments> findByTrainNumber(String trainNumber);

}
