package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.RoomTypeView;
import com.HotelApp.service.RoomTypesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Pageable;


@Controller
public class RoomController {

    private final RoomTypesService roomTypesService;

    public RoomController(RoomTypesService roomTypesService) {
        this.roomTypesService = roomTypesService;
    }

    @GetMapping("/allRoomTypes")
    public String rooms(Model model,
                        @PageableDefault(
                                size = 3,
                                sort = "id"
                        )
                        Pageable pageable) {

        if (roomTypesService.getRoomTypesCount() == 0) {
            roomTypesService.initRoomTypes();
        }
//
//        List<RoomTypeView> allRooms = roomTypesService.getAllRoomTypes();
//        model.addAttribute("allRooms", allRooms);
        Page<RoomTypeView> roomTypes = roomTypesService.getRoomTypes(pageable);
        int totalPages = roomTypes.getTotalPages();
        Page<RoomTypeView> lastPage = roomTypesService.getRoomTypes(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()));

        model.addAttribute("allRooms", roomTypes);
        model.addAttribute("lastPageRooms", lastPage);
        return "rooms";
    }


}
