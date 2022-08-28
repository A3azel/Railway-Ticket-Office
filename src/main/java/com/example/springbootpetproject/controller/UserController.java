package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Orders;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.OrdersService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.springbootpetproject.controller.Paths.PERSONAL_OFFICE_PAGE;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final OrdersService ordersService;

    @Autowired
    public UserController(UserService userService, OrdersService ordersService) {
        this.userService = userService;
        this.ordersService = ordersService;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal){
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("selectedUser",user);
        return PERSONAL_OFFICE_PAGE;
    }

    @GetMapping("/changePassword")
    public String getChangePasswordPage(){
        return null;
    }

    @PostMapping("/changePassword")
    public String getChangePassword(){
        return null;
    }

    /*@GetMapping("/myOrders")
    public String getMyOrdersPage(Model model, Principal principal){
        List<Orders> ordersList =  ordersService.getAllUserOrdersByUserName(principal.getName());
        System.out.println(ordersList);
        model.addAttribute("ordersList",ordersList);
        return "allUserPurchasedTickets";
    }*/

    @GetMapping("/myOrders/page/{pageNumber}")
    public String getMyOrdersPage(Model model, Principal principal
            ,@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 2) Pageable pageable
            ,@PathVariable("pageNumber") int pageNumber){
        Page<Orders> ordersList =  ordersService.getAllUserOrdersByUserNamePage(principal.getName(),pageable,pageNumber);
        List<Orders> ordersListContext = ordersList.getContent();
        /*int totalPages = ordersList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }*/
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",ordersList);
        model.addAttribute("ordersList",ordersListContext);
        return "allUserPurchasedTickets";
    }

    @GetMapping("/myOrders/{id}")
    public String getMyOrdersById(Model model, @PathVariable("id") long id){
        return null;
    }

}
