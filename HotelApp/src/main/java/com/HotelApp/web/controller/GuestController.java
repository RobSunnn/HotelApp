package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.RoomService;
import com.HotelApp.validation.constants.BindingConstants;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService, RoomService roomService) {
        this.guestService = guestService;
    }


    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/add")
    public String add(Model model) {

        if (!model.containsAttribute("addGuestBindingModel")) {
            model.addAttribute("addGuestBindingModel", new AddGuestBindingModel());
        }

        CategoriesEnum[] categories = CategoriesEnum.values();
        model.addAttribute("categories", categories);

//        List<RoomView> availableRooms = roomService.getAvailableRooms();
//        model.addAttribute("availableRooms", availableRooms);

        return "add-guest";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/add")
    public String add(@Valid AddGuestBindingModel addGuestBindingModel,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {

        //TODO: check if documentID exits in database

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingConstants.GUEST_REGISTER_BINDING_MODEL, addGuestBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.GUEST_REGISTER_BINDING_MODEL, bindingResult);

            return "redirect:/guests/add";
        }


        boolean registerGuest = guestService.registerGuest(addGuestBindingModel);

        if (registerGuest) {
            return "redirect:/";
        } else {
            return "redirect:/guests/add";
        }

    }


    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/leave")
    public String leave(Model model) {

        List<GuestEntity> guests = guestService.getAllGuests();
        model.addAttribute("guests", guests);

        return "guest-leave";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/leave")
    public String leave(@RequestParam("roomNumber") Integer roomNumber) {
        guestService.guestWantToLeave(roomNumber);
        return "redirect:/"; // Redirect to home or any other page after processing
    }

}
