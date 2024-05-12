package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.RoomTypeView;
import com.HotelApp.service.RoomTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RoomController {

    private final RoomTypesService roomTypesService;

    public RoomController(RoomTypesService roomTypesService) {
        this.roomTypesService = roomTypesService;
    }

    @GetMapping("/allRoomTypes")
    public String rooms(Model model) {

        if (roomTypesService.getRoomTypesCount() == 0) {
            roomTypesService.initRoomTypes();
        }

        List<RoomTypeView> allRooms = roomTypesService.getAllRoomTypes();
        model.addAttribute("allRooms", allRooms);

        return "rooms";
    }


}
