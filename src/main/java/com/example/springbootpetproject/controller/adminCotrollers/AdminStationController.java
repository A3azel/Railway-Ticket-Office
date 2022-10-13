package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.customExceptions.CityExceptions.CityNotFound;
import com.example.springbootpetproject.customExceptions.StationExceptions.StationAlreadyExist;
import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.service.serviceImplementation.CityService;
import com.example.springbootpetproject.service.serviceImplementation.StationService;
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
import java.util.List;

@Controller
@RequestMapping("/admin/station")
public class AdminStationController {
    private final StationService stationService;

    @Autowired
    public AdminStationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/all/{cityName}/page/{pageNumber}")
    public String getAllStations(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber, @PathVariable("cityName") String cityName
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<StationDTO> stationDTOPage = stationService.getAllStationInCity(cityName,pageable,pageNumber,direction,sort);
        List<StationDTO> stationDTOList = stationDTOPage.getContent();
        model.addAttribute("cityName",cityName);
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",stationDTOPage);
        model.addAttribute("stationDTOList",stationDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allStations";
    }

    @PostMapping("/delete/{cityName}/{id}")
    public String deleteStation(@PathVariable("id") Long id, @PathVariable("cityName") String cityName){
        stationService.deleteStation(id);
        return "redirect:/admin/city/all/page/1";
    }

    @GetMapping("/add/{cityName}")
    public String pageAddStation(@PathVariable("cityName") String cityName, Model model){
        StationDTO stationDTO = new StationDTO();
        stationDTO.setCityName(cityName);
        model.addAttribute("stationDTO", stationDTO);
        return "addStation";
    }

    @GetMapping("/find")
    public String findStation(@RequestParam("stationName")String stationName, Model model){
        Station station = stationService.findStationByStationName(stationName);
        StationDTO selectedStation = stationService.convertStationToStationDTO(station);
        model.addAttribute("stationDTO",selectedStation);
        return "updateStation";
    }

    @PostMapping("/add")
    public String addStation(@Valid @ModelAttribute StationDTO stationDTO, Errors errors, Model model){
        if (errors.hasErrors()){
            return "addStation";
        }
        try {
            stationService.addStation(stationDTO);
        } catch (StationAlreadyExist e) {
            model.addAttribute("StationAlreadyExist" ,e.getMessage());
            return "addStation";
        }
        return "redirect:/admin/station/all/"+stationDTO.getCityName()+"/page/1";
    }

    @PostMapping("/update")
    public String updateStation(@RequestParam("id") Long id, @Valid @ModelAttribute StationDTO stationDTO
            , Errors errors, Model model){
        if (errors.hasErrors()){
            return "updateStation";
        }
        try {
            stationService.updateStation(stationDTO,id);
        } catch (StationAlreadyExist e) {
            model.addAttribute("StationAlreadyExist",e.getMessage());
            return "updateStation";
        } catch (CityNotFound e) {
            model.addAttribute("CityNotFound",e.getMessage());
            return "updateStation";
        }
        return "redirect:/admin/station/all/"+stationDTO.getCityName()+"/page/1";
    }

    @PostMapping("/relevant/{cityName}/{id}")
    public String setRelevant(@PathVariable("id") Long id, @PathVariable("cityName") String cityName){
        stationService.setStationRelevant(id);
        return "redirect:/admin/station/all/"+cityName+"/page/1";
    }
}
