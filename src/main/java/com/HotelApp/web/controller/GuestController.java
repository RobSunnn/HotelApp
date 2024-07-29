package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.HotelApp.common.constants.BindingConstants.GUEST_REGISTER_BINDING_MODEL;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/add")
    public String add(Model model) {
        if (!model.containsAttribute(GUEST_REGISTER_BINDING_MODEL)) {
            model.addAttribute(GUEST_REGISTER_BINDING_MODEL, new AddGuestBindingModel());
        }

        CategoriesEnum[] categories = CategoriesEnum.values();
        model.addAttribute("categories", categories);

        return "moderator/add-guest";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/addGuestSuccess")
    public ModelAndView addGuestSuccess() {
        return new ModelAndView("moderator/add-guest-success");
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add(@Valid AddGuestBindingModel addGuestBindingModel, BindingResult bindingResult) {
        return guestService.registerGuest(addGuestBindingModel, bindingResult);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/leave")
    public String leave(Model model) {
        List<GuestView> guests = guestService.seeAllGuests();
        if (guests.isEmpty()) {
            return "redirect:/moderator";
        }
        model.addAttribute("guests", guests);

        return "moderator/guest-leave";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/leave")
    public String leave(@RequestParam("roomNumber") Integer roomNumber) {
        guestService.checkout(roomNumber);
        return "redirect:/guests/leave";
    }

}
