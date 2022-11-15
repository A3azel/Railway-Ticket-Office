package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.dto.UserDTO;
import com.example.springbootpetproject.entity.User;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final UserServiceI userServiceI;
    private final UserCommentServiceI userCommentServiceI;

    @Autowired
    public AdminUsersController(UserServiceI userServiceI, UserCommentServiceI userCommentServiceI) {
        this.userServiceI = userServiceI;
        this.userCommentServiceI = userCommentServiceI;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllUsersForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserDTO> userDTOPage = userServiceI.getAllUsers(pageable,pageNumber,direction,sort);
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
    public String getSelectedUser(HttpServletRequest request, Model model, @PathVariable("userName") String userName){
        User user = userServiceI.findUserByUsername(userName);
        model.addAttribute("infoAboutPage",request.getParameter("infoAboutPage"));
        model.addAttribute("selectedUser", user);
        return "userInfoForAdmin";
    }

    @PostMapping("/selectedUser/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userServiceI.setUserVerification(userName);
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
        Page<UserCommentDTO> userCommentsDTOPage = userCommentServiceI.findAllUserComments(userName,pageable,pageNumber,direction,sort);
        List<UserCommentDTO> userCommentsList = userCommentsDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userCommentsDTOPage);
        model.addAttribute("userCommentsList",userCommentsList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allUserComments";
    }

    @PostMapping("/{username}/comment/delete/{id}")
    public String deleteUserComment(@PathVariable("username")String username, @PathVariable("id")Long id
            ,@RequestParam("page") int page){
        userCommentServiceI.deleteCommentForAdmin(id);
        return "redirect:/admin/users/selectedUser/"+ URLEncoder.encode(username, StandardCharsets.UTF_8) +"/comment/page/" + page;
    }
}
