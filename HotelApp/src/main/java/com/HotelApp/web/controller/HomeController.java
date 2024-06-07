package com.HotelApp.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("imageUrls", new String[]{"/images/hotel.jpg", "/images/hotel1.jpg", "/images/hotel2.jpg"});
        return "index";
    }
}
