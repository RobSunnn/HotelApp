package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.*;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class HotelController {

    private final HotelService hotelService;

    private final UserService userService;

    public HotelController(HotelService hotelService, UserService userService) {
        this.hotelService = hotelService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session, HttpServletRequest request) {
        Map<String, Integer> infoForHotel = hotelService.getInfoForHotel();
        BigDecimal totalProfit = hotelService.getTotalProfit();
        List<UserView> users = hotelService.findAllUsers();

        model.addAttribute("totalProfit", totalProfit);
        model.addAllAttributes(infoForHotel);
        model.addAttribute("allUsers", users);

        String previousUrl = request.getHeader("referer");
        session.setAttribute("previousUrl", previousUrl);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPanel() {
        return "hotel/admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/allUsers")
    public String allUsersPage() {
        return "hotel/all-users";
    }


    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/hotel/freeRooms")
    public String freeRooms(Model model) {
        List<RoomView> freeRooms = hotelService.seeAllFreeRooms();
        model.addAttribute("freeRooms", freeRooms);

        return "hotel/free-rooms";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/hotel/allGuests")
    public String allGuests(Model model) {
        List<GuestView> allGuests = hotelService.seeAllGuests();
        model.addAttribute("allGuests", allGuests);

        return "hotel/all-guests";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/hotel/allSubscribers")
    public String allSubscribers(Model model) {
        List<SubscriberView> allSubscribers = hotelService.seeAllSubscribers();
        model.addAttribute("allSubscribers", allSubscribers);

        return "hotel/all-subscribers";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/hotel/allHappyGuests")
    public String allHappyGuests(Model model) {
        List<HappyGuestView> allHappyGuests = hotelService.seeAllHappyGuests();
        model.addAttribute("allHappyGuests", allHappyGuests);

        return "hotel/all-happy-guests";
    }

}
