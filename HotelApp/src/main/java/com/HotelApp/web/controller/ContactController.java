package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.service.SubscriberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
        }
        if (!model.containsAttribute("contactRequestBindingModel")) {
            model.addAttribute("contactRequestBindingModel", new ContactRequestBindingModel());
        }
    }

    @GetMapping
    public String contact() {
        return "contact";
    }

    @PostMapping("/subscribe")
    public String subscribe(@Valid AddSubscriberBindingModel addSubscriberBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingConstants.SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.SUBSCRIBER_BINDING_MODEL, bindingResult);

            return "redirect:/contact";
        }

        subscriberService.addNewSubscriber(addSubscriberBindingModel);

        redirectAttributes.addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");

        return "redirect:/contact";
    }

    @PostMapping("/contactForm")
    public String sendMail(@Valid ContactRequestBindingModel contactRequestBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("contactRequestBindingModel", contactRequestBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + "contactRequestBindingModel", bindingResult);

            return "redirect:/contact";
        }

        contactRequestService.sendContactForm(contactRequestBindingModel);

        return "redirect:/";
    }

}
