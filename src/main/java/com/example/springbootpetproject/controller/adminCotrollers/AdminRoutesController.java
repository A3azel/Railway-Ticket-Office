package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.facade.RouteFacade;
import com.example.springbootpetproject.service.serviceImplementation.RouteServiceI;
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
    private final RouteServiceI routeServiceI;
    private final RouteFacade routeFacade;

    @Autowired
    public AdminRoutesController(RouteServiceI routeServiceI, RouteFacade routeFacade) {
        this.routeServiceI = routeServiceI;
        this.routeFacade = routeFacade;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllRouteForAdmin(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<RouteDTO> routeDTOPage = routeServiceI.getAll(pageable,pageNumber,direction,sort);
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
        routeServiceI.deleteRoute(id);
        return "redirect:/admin/route/all/page/1";
    }

    @GetMapping("/update/{id}")
    public String getRouteForAdminByID(Model model, @PathVariable("id") Long id){
        Route route = null;
        try {
            route = routeServiceI.findById(id);
        } catch (RouteNotFound e) {
            model.addAttribute("RouteNotFound",e.getMessage());
            return "forward:/admin/route/all/page/1";
        }
        RouteDTO selectedRoute = routeFacade.convertRouteToRouteDTO(route);
        model.addAttribute("routeDTO",selectedRoute);
        return "changeRouteDetails";
    }

    @PostMapping("/update")
    public String updateRoute(@Valid @ModelAttribute RouteDTO routeDTO, @RequestParam("id") Long id, Errors errors, Model model){
        if(errors.hasErrors()){
            return "changeRouteDetails";
        }
        Map<String,String> errorsMap = routeServiceI.updateRoute(routeDTO,id);
        if(!errorsMap.isEmpty()){
            model.mergeAttributes(errorsMap);
            return "changeRouteDetails";
        }
        return "redirect:/admin/route/all/page/1";
    }

    @GetMapping("/add")
    public String getPageToAddRoute(Model model){
        model.addAttribute("routeDTO", new RouteDTO());
        return "addRoute";
    }

    @PostMapping("/add")
    public String addRoute(@Valid @ModelAttribute RouteDTO routeDTO, Errors errors, Model model){
        if(errors.hasErrors()){
            return "addRoute";
        }

        Map<String,String> errorsMap = routeServiceI.addRoute(routeDTO);
        if(!errorsMap.isEmpty()){
            model.mergeAttributes(errorsMap);
            return "addRoute";
        }
        return "redirect:/admin/route/all/page/1";
    }

    @GetMapping("/find")
    public String findRouteByID(Model model, @RequestParam("id") Long id){
        Route route = null;
        try {
            route = routeServiceI.findById(id);
        } catch (RouteNotFound e) {
            model.addAttribute("RouteNotFound",e.getMessage());
            return "forward:/admin/route/all/page/1";
        }
        RouteDTO selectedRoute = routeFacade.convertRouteToRouteDTO(route);
        model.addAttribute("routeDTO",selectedRoute);
        return "changeRouteDetails";
    }
}
