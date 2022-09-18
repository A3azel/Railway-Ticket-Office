package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.entity.UserComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCommentsServiceInterface {

    void addComment(UserComments userComments);

    void setComment(UserComments userComments);

    void deleteComment(Long id);

    Page<UserCommentsDTO> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber, String direction, String sort);

    Page<UserCommentsDTO> findAllCommentsForTrainByTrainID(Long id, Pageable pageable, int pageNumber, String direction, String sort);

    UserComments findByUserNameAndTrainNumber(String username, String trainNumber);

    List<UserCommentsDTO> findByTrainNumber(String trainNumber);

    Page<UserCommentsDTO> findAllUserComments(String userName, Pageable pageable, int pageNumber, String direction, String sort);

    UserCommentsDTO convertUserCommentsToUserCommentsDTO(UserComments userComments);

}
