package com.HotelApp.service;

import com.HotelApp.domain.models.view.RoomView;

import java.util.List;

public interface RoomService {

    long getCount();

    void initRooms();

    List<RoomView> getAvailableRooms();

    List<RoomView> getAvailableRoomsByType(String roomType);

}
