package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("imageUrls", new String[]{"/images/hotel.jpg", "/images/hotel1.jpg", "/images/hotel2.jpg"});
        return "index";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
        }
    }
}
