package com.example.springbootpetproject.controller.adminCotrollers;

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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/station")
public class AdminStationController {

    private final StationService stationService;
    private final CityService cityService;

    @Autowired
    public AdminStationController(StationService stationService, CityService cityService) {
        this.stationService = stationService;
        this.cityService = cityService;
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
        model.addAttribute("cityName",cityName);
        return "addStation";
    }

    @GetMapping("/find")
    public String findStation(@RequestParam("stationName")String stationName, Model model){
        Station station = stationService.findStationByStationName(stationName);
        StationDTO finderStation = stationService.convertStationToStationDTO(station);
        model.addAttribute("finderStation",finderStation);
        return "updateStation";
    }

    @PostMapping("/add")
    public String addStation(HttpServletRequest request){
        Station newStation = new Station();
        String cityName = request.getParameter("cityName");
        newStation.setStationName(request.getParameter("stationName"));
        newStation.setCity(cityService.findByCityName(cityName));
        stationService.addStation(newStation);
        return "redirect:/admin/station/all/"+cityName+"/page/1";
    }

    @PostMapping("/update")
    public String updateStation(@RequestParam("cityName") String cityName
            , @RequestParam("stationName") String stationName, @RequestParam("id") Long id){
        Station updateStation = stationService.findByID(id);
        updateStation.setStationName(stationName);
        updateStation.setCity(cityService.findByCityName(cityName));
        stationService.updateStation(updateStation);
        return "redirect:/admin/station/all/"+cityName+"/page/1";
    }
}
