package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.service.HotelService;
import com.HotelApp.validation.constants.BindingConstants;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final HotelService hotelService;

    public ContactController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
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

        hotelService.addNewSubscriber(addSubscriberBindingModel);

        redirectAttributes.addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");

        return "redirect:/contact";
    }

}
