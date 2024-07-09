package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.SubscriberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

import static com.HotelApp.common.constants.BindingConstants.CONTACT_REQUEST_BINDING_MODEL;

@Controller
@RequestMapping("/contact")
public class ContactController {
    private static final int TEXT_MAXIMUM_LENGTH = 400;

    private final SubscriberService subscriberService;
    private final ContactRequestService contactRequestService;

    public ContactController(SubscriberService subscriberService, ContactRequestService contactRequestService) {
        this.subscriberService = subscriberService;
        this.contactRequestService = contactRequestService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute(CONTACT_REQUEST_BINDING_MODEL)) {
            model.addAttribute(CONTACT_REQUEST_BINDING_MODEL, new ContactRequestBindingModel());
        }
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
    public ResponseEntity<?> subscribe(@Valid AddSubscriberBindingModel addSubscriberBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        String redirectUrl = request.getHeader("referer").split("8080")[1];

        boolean isSuccessful = subscriberService.addNewSubscriber(
                addSubscriberBindingModel,
                bindingResult,
                redirectAttributes
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (isSuccessful) {
            responseBody.put("success", true);
            responseBody.put("redirectUrl", redirectUrl);
            responseBody.put("message", "Thank you for subscribing!");

            return ResponseEntity.ok().body(responseBody);
        } else {
            responseBody.put("success", false);
            responseBody.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(responseBody);
        }

    }

    @PostMapping("/contactForm")
    @ResponseBody
    public ResponseEntity<?> sendContactRequest(@Valid ContactRequestBindingModel contactRequestBindingModel,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes) {

        boolean isSuccessful = contactRequestService.sendContactForm(
                contactRequestBindingModel,
                bindingResult,
                redirectAttributes
        );
        Map<String, Object> responseBody = new HashMap<>();
        if (isSuccessful) {
            responseBody.put("success", true);
            responseBody.put("redirectUrl", "/contact");
            return ResponseEntity.ok().body(responseBody);
        } else {
            responseBody.put("success", false);
            responseBody.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/onlineReservation")
    public String onlineReservation(@RequestParam("additionalInfo") String additionalInfo,
                                    RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (additionalInfo.length() > TEXT_MAXIMUM_LENGTH) {
            redirectAttributes.addFlashAttribute("errorMessage", "Text is too long.");
            return "redirect:/contact/onlineReservation";
        }

        contactRequestService.makeOnlineReservation(userEmail, additionalInfo);
        return "redirect:/contact/onlineReservationSuccess";
    }

}
