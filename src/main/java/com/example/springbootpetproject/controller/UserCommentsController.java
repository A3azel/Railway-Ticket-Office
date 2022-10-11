package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Orders;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.service.serviceImplementation.OrdersService;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
public class UserCommentsController {
    private final UserCommentsService userCommentsService;
    private final UserService userService;
    private final TrainService trainService;
    private final OrdersService ordersService;

    public UserCommentsController(UserCommentsService userCommentsService, UserService userService, TrainService trainService, OrdersService ordersService) {
        this.userCommentsService = userCommentsService;
        this.userService = userService;
        this.trainService = trainService;
        this.ordersService = ordersService;
    }

    @PostMapping("/add/{trainNumber}")
    public String postComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String userComment = request.getParameter("userComment");
        User user = userService.findUserByUsername(principal.getName());
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        UserComments userCommentObj = new UserComments(userComment, LocalDateTime.now(),user,train);
        if(ordersService.exitByUserNameAndTrainName(principal.getName(),trainNumber) && !userComment.equals("")){
            userCommentsService.addComment(userCommentObj);
            return "redirect:/user/myOrders/" + request.getParameter("id");
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");
    }

    @PostMapping("/update/{trainNumber}")
    public String updateComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String newUserComment = request.getParameter("newComment");
        UserComments userCommentObj = userCommentsService.findByUserNameAndTrainNumber(principal.getName(),trainNumber);
        if(ordersService.exitByUserNameAndTrainName(principal.getName(),trainNumber) && !newUserComment.equals("")){
            userCommentObj.setUserComments(newUserComment);
            userCommentsService.setComment(userCommentObj);
            return "redirect:/user/myOrders/" + request.getParameter("id");
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");
    }

    @PostMapping("/delete/{trainNumber}")
    public String deleteComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        UserComments userComments = userCommentsService.findByUserNameAndTrainNumber(principal.getName(),trainNumber);
        if(ordersService.exitByUserNameAndTrainName(principal.getName(),trainNumber)) {
            userCommentsService.deleteCommentForAdmin(userComments.getId());
            return "redirect:/user/myOrders/" + request.getParameter("id");
        }
        throw new IllegalArgumentException("Коментар не знайдено");
            //userCommentsService.deleteComment(principal.getName(),trainNumber);

    }
}
