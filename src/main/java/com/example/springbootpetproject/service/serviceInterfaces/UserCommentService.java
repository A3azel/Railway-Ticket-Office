package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.orderExceptions.OrderNotFound;
import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.UserComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCommentService {

    void addComment(UserCommentDTO userCommentDTO);

    void setComment(UserCommentDTO userCommentDTO);

    void deleteComment(String username,String trainNumber) throws OrderNotFound;

    void deleteCommentForAdmin(Long id);

    Page<UserCommentDTO> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber, String direction, String sort);

    Page<UserCommentDTO> findAllCommentsForTrainByTrainID(Long id, Pageable pageable, int pageNumber, String direction, String sort);

    UserComment findByUserNameAndTrainNumber(String username, String trainNumber);

    List<UserCommentDTO> findByTrainNumber(String trainNumber);

    Page<UserCommentDTO> findAllUserComments(String userName, Pageable pageable, int pageNumber, String direction, String sort);
}
