package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.UserComment;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
public class UserCommentFacade {
    public UserCommentDTO convertUserCommentsToUserCommentsDTO(UserComment userComment){
        UserCommentDTO userCommentDTO = new UserCommentDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userCommentDTO.setId(userComment.getId());
        userCommentDTO.setCreated(formatter.format(userComment.getCreated()));
        userCommentDTO.setUpdated(formatter.format(userComment.getUpdated()));
        userCommentDTO.setCreatedBy(userComment.getCreatedBy());
        userCommentDTO.setLastModifiedBy(userComment.getLastModifiedBy());
        userCommentDTO.setUsername(userComment.getUser().getUsername());
        userCommentDTO.setTrainNumber(userComment.getTrain().getTrainNumber());
        userCommentDTO.setUserComment(userComment.getUserComment());
        return userCommentDTO;
    }
}
