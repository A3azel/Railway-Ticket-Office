package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.service.serviceImplementation.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/city")
public class AdminCitesController {

    private final CityService cityService;

    @Autowired
    public AdminCitesController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllCites(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<CityDTO> cityPage = cityService.findAllCity(pageable,pageNumber,direction,sort);
        List<CityDTO> cityList = cityPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",cityPage);
        model.addAttribute("cityList",cityList);

        model.addAttribute("sort",sort);
        model.addAttribute("direction",direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allCitesForAdmin";
    }

    @GetMapping("/find")
    public String findCity(@RequestParam("cityName")String cityName){
        return "redirect:/admin/all/stations/" + cityName + "/page/1";
    }

    @GetMapping("/add")
    public String pageAddNewCity(){
        return "addCity";
    }

    @PostMapping("/add")
    public String addNewCity(@RequestParam("cityName") String cityName){
        City newCity = new City();
        newCity.setCityName(cityName);
        newCity.setRelevant(true);
        cityService.addCity(newCity);
        return "redirect:/admin/station/all/" + newCity.getCityName() + "/page/1";
    }

    @PostMapping("/relevant/{id}")
    public String setRelevant(@PathVariable("id") Long id){
        cityService.setCityRelevant(id);
        return "redirect:/admin/city/all/page/1";
    }

}
