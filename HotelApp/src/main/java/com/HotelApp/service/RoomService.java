package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.view.RoomView;

import java.util.List;

public interface RoomService {

    long getCount();

    void initRooms(HotelInfoEntity hotelInfo);

    List<RoomView> getAvailableRoomsByType(String roomType);

    RoomEntity findByRoomNumber(Integer roomNumber);

    void saveRoom(RoomEntity room);
}
