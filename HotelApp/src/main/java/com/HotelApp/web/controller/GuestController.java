package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.HotelService;
import com.HotelApp.common.constants.BindingConstants;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

        if (!model.containsAttribute("addGuestBindingModel")) {
            model.addAttribute("addGuestBindingModel", new AddGuestBindingModel());
        }

        CategoriesEnum[] categories = CategoriesEnum.values();
        model.addAttribute("categories", categories);

        return "moderator/add-guest";
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
            return "redirect:/moderator";
        } else {
            return "redirect:/guests/add";
        }

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
