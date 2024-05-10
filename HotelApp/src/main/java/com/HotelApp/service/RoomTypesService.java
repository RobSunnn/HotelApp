package com.HotelApp.service;

import com.HotelApp.domain.models.view.RoomTypeView;

import java.util.List;

public interface RoomTypesService {

    List<RoomTypeView> getAllRoomTypes();

    long getRoomTypesCount();

    void initRoomTypes();

}
