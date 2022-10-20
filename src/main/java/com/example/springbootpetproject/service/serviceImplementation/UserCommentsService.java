package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.orderExceptions.OrderNotFound;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.repository.UserCommentsRepository;
import com.example.springbootpetproject.service.serviceInterfaces.UserCommentsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentsService implements UserCommentsServiceInterface {
    private final UserCommentsRepository userCommentsRepository;
    private final TrainService trainService;
    private final OrdersService ordersService;
    private final UserService userService;


    @Autowired
    public UserCommentsService(UserCommentsRepository userCommentsRepository, TrainService trainService, OrdersService ordersService, UserService userService) {
        this.userCommentsRepository = userCommentsRepository;
        this.trainService = trainService;
        this.ordersService = ordersService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addComment(UserCommentsDTO userCommentsDTO) {
        String userComment = userCommentsDTO.getUserComments();
        String trainNumber = userCommentsDTO.getTrainNumber();
        String username = userCommentsDTO.getUsername();
        Train train = null;
        try {
            train = trainService.findTrainByTrainNumber(trainNumber);
        } catch (TrainNotFound e) {
            e.printStackTrace();
        }
        if(ordersService.exitByUserNameAndTrainName(username,trainNumber) && !userComment.equals("")){
            UserComments userComments = new UserComments();
            userComments.setUser(userService.findUserByUsername(username));
            userComments.setUserComments(userComment);
            userComments.setTrain(train);
            userCommentsRepository.save(userComments);
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");

    }

    @Override
    @Transactional
    public void setComment(UserCommentsDTO userCommentsDTO) {
        UserComments userComments = new UserComments();
        userCommentsRepository.save(userComments);
    }

    @Override
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(String username, String trainNumber) throws OrderNotFound {
        UserComments userComments = findByUserNameAndTrainNumber(username,trainNumber);
        if(!ordersService.exitByUserNameAndTrainName(username,trainNumber)) {
           throw new OrderNotFound("Order not found");
        }
        userCommentsRepository.deleteById(userComments.getId());
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
    @PreAuthorize("#username == authentication.principal.username")
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
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userCommentsDTO.setId(userComments.getId());
        userCommentsDTO.setCreated(formatter.format(userComments.getCreated()));
        userCommentsDTO.setUpdated(formatter.format(userComments.getUpdated()));
        userCommentsDTO.setCreatedBy(userComments.getCreatedBy());
        userCommentsDTO.setLastModifiedBy(userComments.getLastModifiedBy());
        userCommentsDTO.setUsername(userComments.getUser().getUsername());
        userCommentsDTO.setTrainNumber(userComments.getTrain().getTrainNumber());
        userCommentsDTO.setUserComments(userComments.getUserComments());
        return userCommentsDTO;
    }
}
