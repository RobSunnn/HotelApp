package com.HotelApp.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("moderatorPanel")
public class ModeratorController {

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public String moderatorPanel() {
        return "moderator-panel";
    }
}
