package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String adminPanel(Model model) {

        int freeRoomsCount = adminService.seeAllFreeRooms().size();
        int allGuestsCount = adminService.seeAllGuests().size();
        BigDecimal totalProfit = adminService.getTotalProfit();

        model.addAttribute("freeRoomsCount", freeRoomsCount);
        model.addAttribute("allGuestsCount", allGuestsCount);
        model.addAttribute("totalProfit", totalProfit);

        return "admin-panel";
    }

    @GetMapping("/freeRooms")
    public String freeRooms(Model model) {

        List<RoomView> freeRooms = adminService.seeAllFreeRooms();
        model.addAttribute("freeRooms", freeRooms);

        return "free-rooms";
    }

    @GetMapping("/allGuests")
    public String allGuests(Model model) {

        List<GuestView> allGuests = adminService.seeAllGuests();
        model.addAttribute("allGuests", allGuests);

        return "all-guests";
    }

//    @PostMapping("/makeUserAdmin")
//    public String makeUserAdmin(@RequestParam("userEmail") String userEmail) {
//
//        UserEntity user1 = adminService.findAllUsers().stream().filter(user -> user.getEmail().equals(userEmail)).findFirst().orElseThrow();
//        System.out.println();
//        return "admin-panel";
//    }

}
