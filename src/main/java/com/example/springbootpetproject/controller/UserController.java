package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.Order;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserComment;
import com.example.springbootpetproject.facade.OrderFacade;
import com.example.springbootpetproject.facade.UserCommentFacade;
import com.example.springbootpetproject.facade.UserFacade;
import com.example.springbootpetproject.service.anotherServices.PDFService;
import com.example.springbootpetproject.service.serviceImplementation.OrdersServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.example.springbootpetproject.controller.Paths.PERSONAL_OFFICE_PAGE;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserServiceI userServiceI;
    private final OrdersServiceI ordersServiceI;
    private final UserCommentServiceI userCommentServiceI;
    private final PDFService pdfService;
    private final OrderFacade orderFacade;
    private final UserFacade userFacade;
    private final UserCommentFacade commentFacade;

    @Autowired
    public UserController(UserServiceI userServiceI, OrdersServiceI ordersServiceI, UserCommentServiceI userCommentServiceI, PDFService pdfService, OrderFacade orderFacade, UserFacade userFacade, UserCommentFacade commentFacade) {
        this.userServiceI = userServiceI;
        this.ordersServiceI = ordersServiceI;
        this.userCommentServiceI = userCommentServiceI;
        this.pdfService = pdfService;
        this.orderFacade = orderFacade;
        this.userFacade = userFacade;
        this.commentFacade = commentFacade;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal, HttpSession session){
        User user = userServiceI.findUserByUsername(principal.getName());
        if(session.getAttribute("username")==null){
            session.setAttribute("username",principal.getName());
            session.setAttribute("role",user.getUserRole().name());
            session.setAttribute("balance",user.getUserCountOfMoney());
        }
        UserDTO selectedUser = userFacade.convertUserToUserDTO(user);
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
    public String getChangePassword(HttpServletRequest request, Model model, Principal principal){
        String oldPassword = request.getParameter("oldPass");
        String newPassword = request.getParameter("newPass");
        String conformedNewPassword = request.getParameter("confirmedNewPass");
        Map<String,String> errorsMap = userServiceI.changePassword(oldPassword,newPassword,conformedNewPassword,principal.getName());
        if(!errorsMap.isEmpty()){
            model.mergeAttributes(errorsMap);
            return "changePassword";
        }
        return "redirect:/user";
    }

    @GetMapping("/myOrders/page/{pageNumber}")
    public String getMyOrdersPage(Model model, Principal principal
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<OrderDTO> ordersDTOPage =  ordersServiceI.getAllUserOrdersByUserName(principal.getName(),pageable,pageNumber,direction,sort);
        List<OrderDTO> ordersListContext = ordersDTOPage.getContent();
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
        Order selectedOrder = ordersServiceI.getOrderById(id);
        UserComment comment = userCommentServiceI.findByUserNameAndTrainNumber(principal.getName(),selectedOrder.getRoute().getTrain().getTrainNumber());
        if(selectedOrder.getUser().getUsername().equals(principal.getName())){
            model.addAttribute("selectedOrder",orderFacade .convertOrdersToOrdersDTO(selectedOrder));
            if(comment!=null){
                model.addAttribute("comment",commentFacade.convertUserCommentsToUserCommentsDTO(comment));
            }
        }
        return "userSelectedOrder";
    }

    @PostMapping("/myOrders/pdf/send/{id}")
    public String generateAndSendPDF(@PathVariable("id") long id, Principal principal){
        String userEmail = userServiceI.findUserByUsername(principal.getName()).getUserEmail();
        pdfService.sendTicket(id,principal.getName(),userEmail);
        return "redirect:/user/myOrders/"+id;
    }

    @PostMapping("/myOrders/pdf/{id}")
    public String generateAndDownloadPDF(@PathVariable("id") long id, Principal principal){
        pdfService.generateTicket(id,principal.getName());
        return "redirect:/user/myOrders/"+id;
    }

    @PostMapping("/topUpAccount")
    public String topUpTheAccount(HttpServletRequest request,Principal principal, HttpSession session){
        User user = userServiceI.findUserByUsername(principal.getName());
        String username = principal.getName();
        String moneyString = request.getParameter("countOfMoney");

        BigDecimal money = BigDecimal.valueOf(Long.parseLong(moneyString));
        userServiceI.topUpAccount(money,username);
        session.setAttribute("balance",user.getUserCountOfMoney());
        return "redirect:/user";
    }

}
