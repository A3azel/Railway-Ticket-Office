package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.orderExceptions.OrderNotFound;
import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.service.serviceImplementation.OrdersService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class UserCommentsController {
    private final UserCommentsService userCommentsService;
    private final OrdersService ordersService;

    public UserCommentsController(UserCommentsService userCommentsService, OrdersService ordersService) {
        this.userCommentsService = userCommentsService;
        this.ordersService = ordersService;
    }

    @PostMapping("/add/{trainNumber}")
    public String postComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String userComment = request.getParameter("userComment");
        UserCommentsDTO userCommentsDTO = new UserCommentsDTO();
        userCommentsDTO.setUsername(principal.getName());
        userCommentsDTO.setUserComments(userComment);
        userCommentsDTO.setTrainNumber(trainNumber);
        userCommentsService.addComment(userCommentsDTO);
        return "redirect:/user/myOrders/" + request.getParameter("id");
    }

    @PostMapping("/update/{trainNumber}")
    public String updateComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String newUserComment = request.getParameter("newComment");
        UserComments userCommentObj = userCommentsService.findByUserNameAndTrainNumber(principal.getName(),trainNumber);
        if(ordersService.exitByUserNameAndTrainName(principal.getName(),trainNumber) && !newUserComment.equals("")){
            userCommentObj.setUserComments(newUserComment);
            //userCommentsService.setComment(userCommentObj);
            return "redirect:/user/myOrders/" + request.getParameter("id");
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");
    }

    @PostMapping("/delete/{trainNumber}")
    public String deleteComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        try {
            userCommentsService.deleteComment(principal.getName(),trainNumber);
        } catch (OrderNotFound e) {
            e.printStackTrace();
        }
        return "redirect:/user/myOrders/" + request.getParameter("id");
    }

}
