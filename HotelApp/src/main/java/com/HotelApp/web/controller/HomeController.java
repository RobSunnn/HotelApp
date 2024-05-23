package com.HotelApp.web.controller;

import com.HotelApp.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    private final HotelService hotelService;

    public HomeController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("imageUrls", new String[]{"/images/hotel.jpg", "/images/hotel1.jpg", "/images/hotel2.jpg"});
        return "index";
    }

//    @GetMapping("/home")
//    public String home() {
//        return "home";
//    }

}
