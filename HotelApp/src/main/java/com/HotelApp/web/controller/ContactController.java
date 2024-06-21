package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.SubscriberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.HotelApp.common.constants.BindingConstants.CONTACT_REQUEST_BINDING_MODEL;

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
        if (!model.containsAttribute(CONTACT_REQUEST_BINDING_MODEL)) {
            model.addAttribute(CONTACT_REQUEST_BINDING_MODEL, new ContactRequestBindingModel());
        }
    }

    @GetMapping
    public String contact() {
        return "contact";
    }



    @PostMapping("/subscribe")
    public String subscribe(@Valid @ModelAttribute AddSubscriberBindingModel addSubscriberBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        String redirectUrl = request.getHeader("referer").split("8080")[1];
        subscriberService.addNewSubscriber(addSubscriberBindingModel, bindingResult, redirectAttributes);
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/contactForm")
    public String sendContactRequest(@Valid ContactRequestBindingModel contactRequestBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        contactRequestService.sendContactForm(contactRequestBindingModel, bindingResult, redirectAttributes);
        return "redirect:/contact";
    }
}
