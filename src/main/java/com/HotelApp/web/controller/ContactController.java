package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.SubscriberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final SubscriberService subscriberService;
    private final ContactRequestService contactRequestService;

    public ContactController(SubscriberService subscriberService, ContactRequestService contactRequestService) {
        this.subscriberService = subscriberService;
        this.contactRequestService = contactRequestService;
    }

    @GetMapping
    public String contact() {
        return "contact";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/onlineReservation")
    public String onlineReservation() {
        return "online-reservation";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/onlineReservationSuccess")
    public String onlineReservationSuccess() {
        return "users/online-reservation-success";
    }


    @PostMapping("/subscribe")
    @ResponseBody
    public ResponseEntity<?> subscribe(
            @Valid AddSubscriberBindingModel addSubscriberBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        return subscriberService.addNewSubscriber(addSubscriberBindingModel, bindingResult, redirectAttributes);
    }

    @PostMapping("/contactForm")
    @ResponseBody
    public ResponseEntity<?> sendContactRequest(
            @Valid ContactRequestBindingModel contactRequestBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        return contactRequestService.sendContactForm(contactRequestBindingModel, bindingResult, redirectAttributes);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/onlineReservation")
    public String onlineReservation(
            @RequestParam("additionalInfo") String additionalInfo,
            RedirectAttributes redirectAttributes
    ) {
        return contactRequestService.makeOnlineReservation(additionalInfo, redirectAttributes);
    }
}
