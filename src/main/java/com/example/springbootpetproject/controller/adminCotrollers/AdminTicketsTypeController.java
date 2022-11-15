package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.customExceptions.ticketExeptions.TicketAlreadyExist;
import com.example.springbootpetproject.customExceptions.ticketExeptions.TicketNotFound;
import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.facade.TicketTypeFacade;
import com.example.springbootpetproject.service.serviceImplementation.TicketTypeServiceI;
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
@RequestMapping("/admin/ticket")
public class AdminTicketsTypeController {
    private final TicketTypeServiceI ticketTypeServiceI;
    private final TicketTypeFacade ticketTypeFacade;

    @Autowired
    public AdminTicketsTypeController(TicketTypeServiceI ticketTypeServiceI, TicketTypeFacade ticketTypeFacade) {
        this.ticketTypeServiceI = ticketTypeServiceI;
        this.ticketTypeFacade = ticketTypeFacade;
    }

    @GetMapping("/all/page/{pageNumber}")
    public String getAllTickets(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<TicketTypeDTO> ticketTypeDTOPage = ticketTypeServiceI.getAllTicketTypes(pageable, pageNumber, direction, sort);
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
    public String getTicketForAdminByTicketNumber(HttpServletRequest request, Model model){
        String ticketType = request.getParameter("ticketName");
        TicketType selectedTicket = null;
        try {
            selectedTicket = ticketTypeServiceI.findByTicketName(ticketType);
        } catch (TicketNotFound e) {
            model.addAttribute("TicketNotFound", e.getMessage());
            return "forward:/admin/ticket/all/page/1";
        }
        TicketTypeDTO selectedTicketDTO = ticketTypeFacade.convertTicketTypeToTicketTypeDTO(selectedTicket);
        model.addAttribute("ticketTypeDTO", selectedTicketDTO);
        return "updateTicketInfo";
    }

    @PostMapping("/update")
    public String updateInfoAboutTicket(@RequestParam("id") Long id, @Valid @ModelAttribute TicketTypeDTO ticketTypeDTO
            , Errors errors, Model model){
        if (errors.hasErrors()) {
            return "updateTicketInfo";
        }
        try {
            ticketTypeServiceI.updateTicketInfo(ticketTypeDTO,id);
        } catch (TicketAlreadyExist e) {
            model.addAttribute("TicketAlreadyExist", e.getMessage());
            return "updateTicketInfo";
        }
        return "redirect:/admin/ticket/all/page/1";
    }

    @GetMapping("/{id}")
    public String getTicketById(Model model, @PathVariable("id") Long id){
        TicketTypeDTO selectedTicket = ticketTypeFacade
                .convertTicketTypeToTicketTypeDTO(ticketTypeServiceI.getTicketById(id));
        model.addAttribute("ticketTypeDTO",selectedTicket);
        return "updateTicketInfo";
    }

    @GetMapping("/add")
    public String pageAddTicket(Model model){
        model.addAttribute("ticketType", new TicketType());
        return "addTicket";
    }

    @PostMapping("/add")
    public String addTicket(@Valid @ModelAttribute TicketType ticketType, Errors errors, Model model){
        if(errors.hasErrors()){
            return "addTicket";
        }
        try {
            ticketTypeServiceI.addTicketType(ticketType);
        } catch (TicketAlreadyExist e) {
            model.addAttribute("TicketAlreadyExist", e.getMessage());
            return "addTicket";
        }
        return "redirect:/admin/ticket/all/page/1";
    }

    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable("id") Long id){
        ticketTypeServiceI.deleteTicketById(id);
        return "redirect:/admin/ticket/all/page/1";
    }

    @PostMapping("/relevant/{id}")
    public String setRelevant(@PathVariable("id") Long id){
        ticketTypeServiceI.setTicketRelevant(id);
        return "redirect:/admin/ticket/all/page/1";
    }
}
