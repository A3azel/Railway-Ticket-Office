package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.service.serviceImplementation.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/ticket")
public class AdminTicketsTypeController {
    private final TicketTypeService ticketTypeService;

    @Autowired
    public AdminTicketsTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllTickets(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<TicketTypeDTO> ticketTypeDTOPage = ticketTypeService.getAllTicketTypes(pageable, pageNumber, direction, sort);
        List<TicketTypeDTO> ticketTypeDTOList = ticketTypeDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",ticketTypeDTOPage);
        model.addAttribute("ticketTypeList",ticketTypeDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allTicketsForAdmin";
    }

    @GetMapping("/find")
    public String getTrainForAdminByTrainNumber(HttpServletRequest request, Model model){
        String ticketType = request.getParameter("ticketName");
        TicketType selectedTicket = ticketTypeService.getTicketByTicketType(ticketType);
        //System.out.println(selectedTicket);
        TicketTypeDTO selectedTicketDTO = ticketTypeService.convertTicketTypeToTicketTypeDTO(selectedTicket);
        model.addAttribute("selectedTicket", selectedTicketDTO);
        return "updateTicketInfo";
    }

    @PostMapping("/update")
    public String updateInfoAboutTrain(@RequestParam Map<String,String> allParam){
        ticketTypeService.updateTicketInfo(allParam);
        return "redirect:/admin/ticket/" + allParam.get("id");
    }

    @GetMapping("/{id}")
    public String getTicketById(Model model, @PathVariable("id") Long id){
        TicketTypeDTO selectedTicket = ticketTypeService
                .convertTicketTypeToTicketTypeDTO(ticketTypeService.getTicketById(id));
        model.addAttribute("selectedTicket",selectedTicket);
        return "updateTicketInfo";
    }

    @GetMapping("/add")
    public String addTicket(){
        return "addTicket";
    }

    @PostMapping("/add")
    public String addTrain(@RequestParam Map<String,String> allParam){
        ticketTypeService.addTicketType(allParam);
        return "redirect:/admin/ticket/all/page/1";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        ticketTypeService.deleteTicketById(id);
        return "redirect:/admin/ticket/all/page/1";
    }




}
