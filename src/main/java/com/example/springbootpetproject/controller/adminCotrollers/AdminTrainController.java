package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
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
@RequestMapping("/admin/train")
public class AdminTrainController {
    private final TrainService trainService;

    @Autowired
    public AdminTrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllTrainsForAdmin(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<TrainDTO> trainDTOPage = trainService.getAllTrain(pageable,pageNumber,direction,sort);
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
        Train train = trainService.findTrainByID(Long.parseLong(id));
        TrainDTO selectedTrain = trainService.convertTrainToTrainDTO(train);
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @GetMapping("/find/byTrainNumber")
    public String getTrainForAdminByTrainNumber(HttpServletRequest request, Model model){
        String trainNumber = request.getParameter("wantedTrain");
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        TrainDTO selectedTrain = trainService.convertTrainToTrainDTO(train);
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @PostMapping("/update")
    public String updateInfoAboutTrain(@RequestParam("id") Long id, @RequestParam("trainNumber") String trainNumber
            ,@RequestParam("numberOfCompartmentSeats") int numberOfCompartmentSeats
            ,@RequestParam("numberOfSuiteSeats") int numberOfSuiteSeats){
        Train oldTrain = trainService.findTrainByID(id);
        oldTrain.setTrainNumber(trainNumber);
        oldTrain.setNumberOfCompartmentSeats(numberOfCompartmentSeats);
        oldTrain.setNumberOfSuiteSeats(numberOfSuiteSeats);
        trainService.updateTrain(oldTrain);
        return "redirect:/admin/train/" + id;
    }

    @GetMapping("/add")
    public String getPageToAddTrain(){
        return "addTrain";
    }

    @PostMapping("/add")
    public String addTrain(@RequestParam("trainNumber") String trainNumber
            ,@RequestParam("numberOfCompartmentSeats") int numberOfCompartmentSeats
            ,@RequestParam("numberOfSuiteSeats") int numberOfSuiteSeats){
        Train newTrain = new Train();
        newTrain.setTrainNumber(trainNumber);
        newTrain.setNumberOfCompartmentSeats(numberOfCompartmentSeats);
        newTrain.setNumberOfSuiteSeats(numberOfSuiteSeats);
        trainService.addTrain(newTrain);
        return "redirect:/admin/train/all/page/1";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        trainService.deleteTrainByID(id);
        return "redirect:/admin/train/all/page/1";
    }
}
