package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.repository.UserCommentsRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserCommentsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentsService implements UserCommentsServiceInterface {
    private final UserCommentsRepository userCommentsRepository;

    @Autowired
    public UserCommentsService(UserCommentsRepository userCommentsRepository) {
        this.userCommentsRepository = userCommentsRepository;
    }

    @Override
    @Transactional
    public void addComment(UserComments userComments) {
        userCommentsRepository.save(userComments);
    }

    @Override
    @Transactional
    public void setComment(UserComments userComments) {
        userCommentsRepository.save(userComments);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        userCommentsRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserComments> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userCommentsRepository.findAllByTrain_TrainNumber(trainNumber,changePageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UserComments findByUserNameAndTrainNumber(String username, String trainNumber) {
        return userCommentsRepository.findByUserUsernameAndTrainTrainNumber(username, trainNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCommentsDTO> findByTrainNumber(String trainNumber) {
        List<UserComments> userCommentsList = userCommentsRepository.findByTrain_TrainNumber(trainNumber);
        return userCommentsList.stream().map(this::convertUserCommentsToUserCommentsDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentsDTO> findAllUserComments(String userName, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<UserComments> userCommentsPage = userCommentsRepository.findAllByUserUsername(userName,changePageable);
        Page<UserCommentsDTO> userCommentsDTOPage = userCommentsPage.map(this::convertUserCommentsToUserCommentsDTO);
        return userCommentsDTOPage;
    }

    @Override
    public UserCommentsDTO convertUserCommentsToUserCommentsDTO(UserComments userComments){
        UserCommentsDTO userCommentsDTO = new UserCommentsDTO();
        userCommentsDTO.setId(userComments.getId());
        userCommentsDTO.setUsername(userComments.getUser().getUsername());
        userCommentsDTO.setTrainNumber(userComments.getTrain().getTrainNumber());
        userCommentsDTO.setUserComments(userComments.getUserComments());
        userCommentsDTO.setPublicationTime(userComments.getPublicationTime());
        return userCommentsDTO;
    }
}
