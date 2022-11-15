package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainAlreadyExist;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.facade.TrainFacade;
import com.example.springbootpetproject.service.serviceImplementation.TrainServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/train")
public class AdminTrainController {
    private final TrainServiceI trainServiceI;
    private final UserCommentServiceI userCommentServiceI;
    private final TrainFacade trainFacade;

    @Autowired
    public AdminTrainController(TrainServiceI trainServiceI, UserCommentServiceI userCommentServiceI, TrainFacade trainFacade) {
        this.trainServiceI = trainServiceI;
        this.userCommentServiceI = userCommentServiceI;
        this.trainFacade = trainFacade;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllTrainsForAdmin(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<TrainDTO> trainDTOPage = trainServiceI.getAllTrain(pageable,pageNumber,direction,sort);
        List<TrainDTO> trainList = trainDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",trainDTOPage);
        model.addAttribute("trainList",trainList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allTrainsForAdmin";
    }

    @GetMapping("/{id}")
    public String getTrainForAdminByID(Model model, @PathVariable("id") String id){
        Train train = trainServiceI.findTrainByID(Long.parseLong(id));
        TrainDTO selectedTrain = trainFacade.convertTrainToTrainDTO(train);
        model.addAttribute("trainDTO",selectedTrain);
        return "changeTrainDetails";
    }

    @GetMapping("/find/byTrainNumber")
    public String getTrainForAdminByTrainNumber(HttpServletRequest request, Model model){
        String trainNumber = request.getParameter("wantedTrain");
        Train train = null;
        try {
            train = trainServiceI.findTrainByTrainNumber(trainNumber);
        } catch (TrainNotFound e) {
            model.addAttribute("TrainNotFound", e.getMessage());
            return "forward:/admin/train/all/page/1";
        }
        TrainDTO selectedTrain = trainFacade.convertTrainToTrainDTO(train);
        model.addAttribute("trainDTO",selectedTrain);
        return "changeTrainDetails";
    }

    @PostMapping("/update")
    public String updateInfoAboutTrain(@RequestParam("id") Long id, @Valid @ModelAttribute TrainDTO trainDTO, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "changeTrainDetails";
        }
        try {
            trainServiceI.updateTrain(trainDTO,id);
        } catch (TrainAlreadyExist e) {
            model.addAttribute("TrainAlreadyExist", e.getMessage());
            return "changeTrainDetails";
        }
        return "redirect:/admin/train/all/page/1";
    }


    @GetMapping("/add")
    public String getPageToAddTrain(Model model){
        model.addAttribute("train", new Train());
        return "addTrain";
    }

    @PostMapping("/add")
    public String addTrain(@Valid @ModelAttribute Train train, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "addTrain";
        }
        try {
            trainServiceI.addTrain(train);
        } catch (TrainAlreadyExist e) {
            model.addAttribute("TrainAlreadyExist", e.getMessage());
            return "addTrain";
        }
        return "redirect:/admin/train/all/page/1";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        trainServiceI.deleteTrainByID(id);
        return "redirect:/admin/train/all/page/1";
    }

    @GetMapping("/all/comment/{id}/page/{pageNumber}")
    public String getAllCommentsAboutTrain(@PathVariable("id") Long id, Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserCommentDTO> userCommentsDTOPage = userCommentServiceI.findAllCommentsForTrainByTrainID(id, pageable, pageNumber, direction, sort);
        List<UserCommentDTO> userCommentsDTOList = userCommentsDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userCommentsDTOPage);
        model.addAttribute("userCommentsList",userCommentsDTOList);
        model.addAttribute("trainId",id);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allCommentsAboutTrain";
    }

    @PostMapping("/delete/{trainId}/comment/{commentId}")
    public String deleteComment(@PathVariable("trainId") Long trainId, @PathVariable("commentId") Long commentId, Principal principal){
        userCommentServiceI.deleteCommentForAdmin(commentId);
        return "redirect:/admin/train/all/comment/" + trainId + "/page/1";
    }

    @PostMapping("/relevant/{id}")
    public String setRelevant(@PathVariable("id") Long id){
        trainServiceI.setTrainRelevant(id);
        return "redirect:/admin/train/all/page/1";
    }
}
