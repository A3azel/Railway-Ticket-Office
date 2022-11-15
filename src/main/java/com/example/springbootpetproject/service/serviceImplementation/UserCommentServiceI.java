package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.orderExceptions.OrderNotFound;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.UserComment;
import com.example.springbootpetproject.facade.UserCommentFacade;
import com.example.springbootpetproject.repository.UserCommentsRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentServiceI implements UserCommentService {
    private final UserCommentsRepository userCommentsRepository;
    private final TrainServiceI trainServiceI;
    private final OrdersServiceI ordersServiceI;
    private final UserServiceI userServiceI;
    private final UserCommentFacade userCommentFacade;


    @Autowired
    public UserCommentServiceI(UserCommentsRepository userCommentsRepository, TrainServiceI trainServiceI, OrdersServiceI ordersServiceI, UserServiceI userServiceI, UserCommentFacade userCommentFacade) {
        this.userCommentsRepository = userCommentsRepository;
        this.trainServiceI = trainServiceI;
        this.ordersServiceI = ordersServiceI;
        this.userServiceI = userServiceI;
        this.userCommentFacade = userCommentFacade;
    }

    @Override
    @Transactional
    public void addComment(UserCommentDTO userCommentDTO) {
        String userComment = userCommentDTO.getUserComment();
        String trainNumber = userCommentDTO.getTrainNumber();
        String username = userCommentDTO.getUsername();
        Train train = null;
        try {
            train = trainServiceI.findTrainByTrainNumber(trainNumber);
        } catch (TrainNotFound e) {
            e.printStackTrace();
        }
        if(ordersServiceI.exitByUserNameAndTrainName(username,trainNumber) && !userComment.equals("")){
            UserComment userComments = new UserComment();
            userComments.setUser(userServiceI.findUserByUsername(username));
            userComments.setUserComment(userComment);
            userComments.setTrain(train);
            userCommentsRepository.save(userComments);
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");

    }

    @Override
    @Transactional
    public void setComment(UserCommentDTO userCommentDTO) {
        UserComment userComment = new UserComment();
        userCommentsRepository.save(userComment);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(String username, String trainNumber) throws OrderNotFound {
        UserComment userComment = findByUserNameAndTrainNumber(username,trainNumber);
        if(!ordersServiceI.exitByUserNameAndTrainName(username,trainNumber)) {
           throw new OrderNotFound("Order not found");
        }
        userCommentsRepository.deleteById(userComment.getId());
    }

    /*@Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(String username,String trainNumber) {
        UserComments userComments = userCommentsService.findByUserNameAndTrainNumber(username,trainNumber);
        if(ordersService.exitByUserNameAndTrainName(username,trainNumber)) {
            userCommentsRepository.deleteById(userComments.getId());
        }
        throw new IllegalArgumentException("Коментар не знайдено");
    }*/

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ADMIN')")
    public void deleteCommentForAdmin(Long id) {
        userCommentsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentDTO> findAllCommentsForTrain(String trainNumber, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userCommentsRepository.findAllByTrain_TrainNumber(trainNumber,changePageable)
                .map(userCommentFacade::convertUserCommentsToUserCommentsDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentDTO> findAllCommentsForTrainByTrainID(Long id, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return userCommentsRepository.findAllByTrain_Id(id,changePageable)
                .map(userCommentFacade::convertUserCommentsToUserCommentsDTO);
    }


    //???
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("#username == authentication.principal.username")
    public UserComment findByUserNameAndTrainNumber(String username, String trainNumber) {
        return userCommentsRepository.findByUserUsernameAndTrainTrainNumber(username, trainNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCommentDTO> findByTrainNumber(String trainNumber) {
        List<UserComment> userCommentsList = userCommentsRepository.findByTrain_TrainNumber(trainNumber);
        return userCommentsList.stream().map(userCommentFacade::convertUserCommentsToUserCommentsDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCommentDTO> findAllUserComments(String userName, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<UserComment> userCommentsPage = userCommentsRepository.findAllByUserUsername(userName,changePageable);
        Page<UserCommentDTO> userCommentsDTOPage = userCommentsPage.map(userCommentFacade::convertUserCommentsToUserCommentsDTO);
        return userCommentsDTOPage;
    }

}
