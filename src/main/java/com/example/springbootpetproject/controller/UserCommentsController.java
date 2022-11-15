package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.orderExceptions.OrderNotFound;
import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.UserComment;
import com.example.springbootpetproject.service.serviceImplementation.OrdersServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class UserCommentsController {
    private final UserCommentServiceI userCommentServiceI;
    private final OrdersServiceI ordersServiceI;

    public UserCommentsController(UserCommentServiceI userCommentServiceI, OrdersServiceI ordersServiceI) {
        this.userCommentServiceI = userCommentServiceI;
        this.ordersServiceI = ordersServiceI;
    }

    @PostMapping("/add/{trainNumber}")
    public String postComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String userComment = request.getParameter("userComment");
        UserCommentDTO userCommentDTO = new UserCommentDTO();
        userCommentDTO.setUsername(principal.getName());
        userCommentDTO.setUserComment(userComment);
        userCommentDTO.setTrainNumber(trainNumber);
        userCommentServiceI.addComment(userCommentDTO);
        return "redirect:/user/myOrders/" + request.getParameter("id");
    }

    @PostMapping("/update/{trainNumber}")
    public String updateComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        String newUserComment = request.getParameter("newComment");
        UserComment userCommentObj = userCommentServiceI.findByUserNameAndTrainNumber(principal.getName(),trainNumber);
        if(ordersServiceI.exitByUserNameAndTrainName(principal.getName(),trainNumber) && !newUserComment.equals("")){
            userCommentObj.setUserComment(newUserComment);
            //userCommentsService.setComment(userCommentObj);
            return "redirect:/user/myOrders/" + request.getParameter("id");
        }
        throw new IllegalArgumentException("Користувач не робив данного замовлення");
    }

    @PostMapping("/delete/{trainNumber}")
    public String deleteComment(HttpServletRequest request, Principal principal, @PathVariable("trainNumber") String trainNumber){
        try {
            userCommentServiceI.deleteComment(principal.getName(),trainNumber);
        } catch (OrderNotFound e) {
            e.printStackTrace();
        }
        return "redirect:/user/myOrders/" + request.getParameter("id");
    }

}
