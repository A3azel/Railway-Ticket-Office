package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.repository.UserCommentsRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserCommentsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentsService implements UserCommentsServiceInterface {
    private final UserCommentsRepository userCommentsRepository;

    @Autowired
    public UserCommentsService(UserCommentsRepository userCommentsRepository) {
        this.userCommentsRepository = userCommentsRepository;
    }

    @Override
    public void addComment(UserComments userComments) {
        userCommentsRepository.save(userComments);
    }

    @Override
    public void setComment(UserComments userComments) {
        userCommentsRepository.save(userComments);
    }

    @Override
    public void deleteComment(Long id) {
        userCommentsRepository.deleteById(id);

    }

    @Override
    public Page<UserComments> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return userCommentsRepository.findAllByTrain_TrainNumber(trainNumber,changePageable);
    }

    @Override
    public UserComments findByUserNameAndTrainNumber(String username, String trainNumber) {
        return userCommentsRepository.findByUserUsernameAndTrainTrainNumber(username, trainNumber);
    }

    @Override
    public List<UserComments> findByTrainNumber(String trainNumber) {
        return userCommentsRepository.findByTrain_TrainNumber(trainNumber);
    }
}
