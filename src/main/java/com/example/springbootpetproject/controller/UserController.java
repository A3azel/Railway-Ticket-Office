package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.dto.OrdersDTO;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.Orders;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.service.serviceImplementation.OrdersService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static com.example.springbootpetproject.controller.Paths.PERSONAL_OFFICE_PAGE;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final OrdersService ordersService;
    private final UserCommentsService userCommentsService;

    @Autowired
    public UserController(UserService userService, OrdersService ordersService, UserCommentsService userCommentsService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.userCommentsService = userCommentsService;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal){
        User user = userService.findUserByUsername(principal.getName());
        UserDTO selectedUser = userService.convertUserToUserDTO(user);
        model.addAttribute("selectedUser",selectedUser);
        if(user.getUserRole().name().equals("USER")){
            return PERSONAL_OFFICE_PAGE;
        }
        return "adminPersonalOffice";
    }

    @GetMapping("/changePassword")
    public String getChangePasswordPage(){
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String getChangePassword(){
        return "redirect:/user";
    }

    @GetMapping("/myOrders/page/{pageNumber}")
    public String getMyOrdersPage(Model model, Principal principal
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<OrdersDTO> ordersDTOPage =  ordersService.getAllUserOrdersByUserName(principal.getName(),pageable,pageNumber,direction,sort);
        List<OrdersDTO> ordersListContext = ordersDTOPage.getContent();
        /*int totalPages = ordersList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }*/
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",ordersDTOPage);
        model.addAttribute("ordersList",ordersListContext);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allUserPurchasedTickets";
    }

    @GetMapping("/myOrders/{id}")
    public String getMyOrdersById(Model model, @PathVariable("id") long id, Principal principal){
        Orders selectedOrder = ordersService.getOrderById(id);
        UserComments comment = userCommentsService.findByUserNameAndTrainNumber(principal.getName(),selectedOrder.getRoute().getTrain().getTrainNumber());
        if(selectedOrder.getUser().getUsername().equals(principal.getName())){
            model.addAttribute("selectedOrder",selectedOrder);
            model.addAttribute("comment",comment);
        }
        return "userSelectedOrder";
    }

    @PostMapping("/topUpAccount")
    public String topUpTheAccount(HttpServletRequest request,Principal principal){
        String username = principal.getName();
        String moneyString = request.getParameter("countOfMoney");
        BigDecimal money = BigDecimal.valueOf(Long.parseLong(moneyString));
        userService.topUpAccount(money,username);
        return "redirect:/user";
    }

}
