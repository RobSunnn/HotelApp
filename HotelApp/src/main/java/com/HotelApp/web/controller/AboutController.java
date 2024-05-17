package com.HotelApp.web.controller;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.service.SubscriberService;
import com.HotelApp.validation.constants.BindingConstants;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AboutController {

    private final SubscriberService subscriberService;

    public AboutController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/about")
    public String about(Model model) {
        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
        }
        return "about";
    }

    @PostMapping("/about/subscribe")
    public String subscribe(@ModelAttribute("subscriber") SubscriberEntity subscriber,
            @Valid AddSubscriberBindingModel addSubscriberBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingConstants.SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.SUBSCRIBER_BINDING_MODEL, bindingResult);

            return "redirect:/about";
        }

        subscriberService.addNewSubscriber(addSubscriberBindingModel);

        redirectAttributes.addFlashAttribute("successMessage", "Thank you for subscribing!");

        return "redirect:/about";
    }

}
