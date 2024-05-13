package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFreeRoomsRestController {

    private final RoomService roomService;

    public GetFreeRoomsRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/api/rooms/{category}")
    public ResponseEntity<List<RoomView>> rooms(@PathVariable("category") String category) {
        return ResponseEntity.ok(roomService.getAvailableRoomsByType(category));
    }
}
