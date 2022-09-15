package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final UserService userService;
    private final UserCommentsService userCommentsService;

    @Autowired
    public AdminUsersController(UserService userService, UserCommentsService userCommentsService) {
        this.userService = userService;
        this.userCommentsService = userCommentsService;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllUsersForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserDTO> userDTOPage = userService.getAllUsers(pageable,pageNumber,direction,sort);
        List<UserDTO> userList = userDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userDTOPage);
        model.addAttribute("userList",userList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allUsers";
    }

    @GetMapping("/selectedUser/{userName}")
    public String getSelectedUser(Model model, @PathVariable("userName") String userName){
        User user = userService.findUserByUsername(userName);
        model.addAttribute("selectedUser", user);
        return "userInfoForAdmin";
    }

    @PostMapping("/selectedUser/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userService.setUserVerification(userName);
        String pageNumber = request.getParameter("infoAboutPage");
        return "redirect:/admin/users/all/page/" + pageNumber;
    }

    @GetMapping("/selectedUser/order")
    public String getAllUserOrdersForAdmin(){
        return null;
    }

    @GetMapping("/selectedUser/{userName}/comment/page/{pageNumber}")
    public String getAllUserCommentsForAdmin(@PathVariable("userName") String userName, Model model
            , Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserCommentsDTO> userCommentsDTOPage = userCommentsService.findAllUserComments(userName,pageable,pageNumber,direction,sort);
        List<UserCommentsDTO> userCommentsList = userCommentsDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userCommentsDTOPage);
        model.addAttribute("userCommentsList",userCommentsList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allUserComments";
    }

    @PostMapping("/selectedUser/comment/{id}")
    public String deleteUserComment(Model model,@PathVariable("id")Long id){
        return null;
    }
}
