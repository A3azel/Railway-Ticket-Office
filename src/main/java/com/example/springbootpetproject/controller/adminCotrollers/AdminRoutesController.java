package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.customExceptions.routeExceptions.DataCompareError;
import com.example.springbootpetproject.customExceptions.routeExceptions.ProblemWithSeatsCount;
import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.customExceptions.stationExceptions.StationNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.service.serviceImplementation.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/route")
public class AdminRoutesController {
    private final RouteService routeService;

    @Autowired
    public AdminRoutesController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllRouteForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<RouteDTO> routeDTOPage = routeService.getAll(pageable,pageNumber,direction,sort);
        List<RouteDTO> routeDTOList = routeDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",routeDTOPage);
        model.addAttribute("routeDTOList",routeDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allRoute";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoute(@PathVariable("id") Long id){
        routeService.deleteRoute(id);
        return "redirect:/admin/route/all/page/1";
    }

    @GetMapping("/update/{id}")
    public String getRouteForAdminByID(Model model, @PathVariable("id") Long id){
        Route route = null;
        try {
            route = routeService.findById(id);
        } catch (RouteNotFound e) {
            model.addAttribute("RouteNotFound",e.getMessage());
            return "forward:/admin/route/all/page/1";
        }
        RouteDTO selectedRoute = routeService.convertRouteToRouteDTO(route);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }

    @PostMapping("/update/{id}")
    public String updateRoute(@RequestParam Map<String,String> allParam, @PathVariable("id") Long id){
        routeService.updateRoute(allParam);
        return "redirect:/admin/route/update/" + id;
    }

    @GetMapping("/add")
    public String getPageToAddRoute(Model model){
        model.addAttribute("RouteDTO", new RouteDTO());
        return "addRoute";
    }

    @PostMapping("/add")
    public String addRoute(@Valid @ModelAttribute RouteDTO routeDTO, Errors errors, Model model){
        if(errors.hasErrors()){
            return "addRoute";
        }
        try {
            routeService.addRoute(routeDTO);
        } catch (StationNotFound e) {
            e.printStackTrace();
        } catch (DataCompareError e) {
            e.printStackTrace();
        } catch (ProblemWithSeatsCount e) {
            e.printStackTrace();
        }
        return "redirect:/admin/all/route/page/1";
    }

    @GetMapping("/find")
    public String findRouteByID(Model model, @RequestParam("id") Long id){
        Route route = null;
        try {
            route = routeService.findById(id);
        } catch (RouteNotFound e) {
            model.addAttribute("RouteNotFound",e.getMessage());
            return "forward:/admin/route/all/page/1";
        }
        RouteDTO selectedRoute = routeService.convertRouteToRouteDTO(route);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }
}
