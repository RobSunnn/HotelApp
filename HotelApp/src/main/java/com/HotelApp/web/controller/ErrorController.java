package com.HotelApp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "redirect:/session-expired?expired=true";
    }
}
