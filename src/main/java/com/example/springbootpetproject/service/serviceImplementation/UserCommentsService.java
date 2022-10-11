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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentsService implements UserCommentsServiceInterface {
    private final UserCommentsRepository userCommentsRepository;
    private final UserCommentsService userCommentsService;
    private final OrdersService ordersService;

    @Autowired
    public UserCommentsService(UserCommentsRepository userCommentsRepository, UserCommentsService userCommentsService, OrdersService ordersService) {
        this.userCommentsRepository = userCommentsRepository;
        this.userCommentsService = userCommentsService;
        this.ordersService = ordersService;
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
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(String username,String trainNumber) {
        UserComments userComments = userCommentsService.findByUserNameAndTrainNumber(username,trainNumber);
        if(ordersService.exitByUserNameAndTrainName(username,trainNumber)) {
            userCommentsRepository.deleteById(userComments.getId());
        }
        throw new IllegalArgumentException("Коментар не знайдено");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCommentForAdmin(Long id) {
        userCommentsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentsDTO> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userCommentsRepository.findAllByTrain_TrainNumber(trainNumber,changePageable)
                .map(this::convertUserCommentsToUserCommentsDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentsDTO> findAllCommentsForTrainByTrainID(Long id, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userCommentsRepository.findAllByTrain_Id(id,changePageable)
                .map(this::convertUserCommentsToUserCommentsDTO);
    }

    //???
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') || " +
            "#username == authentication.principal.username")
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
