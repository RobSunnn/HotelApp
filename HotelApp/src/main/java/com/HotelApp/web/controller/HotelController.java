package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.domain.models.view.HappyGuestView;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.domain.models.view.SubscriberView;
import com.HotelApp.service.HotelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String adminPanel(Model model) {
        int freeRoomsCount = hotelService.seeAllFreeRooms().size();
        int allGuestsCount = hotelService.seeAllGuests().size();
        int totalSubscribers = hotelService.seeAllSubscribers().size();
        int happyGuestsCount = hotelService.seeAllHappyGuests().size();
        BigDecimal totalProfit = hotelService.getTotalProfit();

        model.addAttribute("freeRoomsCount", freeRoomsCount);
        model.addAttribute("allGuestsCount", allGuestsCount);
        model.addAttribute("happyGuestsCount", happyGuestsCount);
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("totalSubscribers", totalSubscribers);
        return "admin/admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/freeRooms")
    public String freeRooms(Model model) {
        List<RoomView> freeRooms = hotelService.seeAllFreeRooms();
        model.addAttribute("freeRooms", freeRooms);

        return "admin/free-rooms";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allGuests")
    public String allGuests(Model model) {
        List<GuestView> allGuests = hotelService.seeAllGuests();
        model.addAttribute("allGuests", allGuests);

        return "admin/all-guests";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allSubscribers")
    public String allSubscribers(Model model) {
        List<SubscriberView> allSubscribers = hotelService.seeAllSubscribers();
        model.addAttribute("allSubscribers", allSubscribers);

        return "admin/all-subscribers";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allHappyGuests")
    public String allHappyGuests(Model model) {
        List<HappyGuestView> allHappyGuests = hotelService.seeAllHappyGuests();
        model.addAttribute("allHappyGuests", allHappyGuests);

        return "admin/all-happy-guests";
    }

}