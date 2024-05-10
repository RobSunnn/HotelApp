package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.RoomTypeView;
import com.HotelApp.service.RoomService;
import com.HotelApp.service.RoomTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RoomController {

    private final RoomTypesService roomTypesService;

    private final RoomService roomService;

    public RoomController(RoomTypesService roomTypesService, RoomService roomService) {
        this.roomTypesService = roomTypesService;
        this.roomService = roomService;
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
