package com.HotelApp.web.controller;

import com.HotelApp.service.RoomTypesService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    private final RoomTypesService roomTypesService;

    public RoomController(RoomTypesService roomTypesService) {
        this.roomTypesService = roomTypesService;
    }

    @GetMapping("/allRoomTypes")
    public String rooms(Model model,
                        @PageableDefault(
                                size = 2,
                                sort = "id"
                        )
                        Pageable pageable) {
        roomTypesService.getRooms(pageable, model);
        return "rooms/rooms-page";
    }
}
