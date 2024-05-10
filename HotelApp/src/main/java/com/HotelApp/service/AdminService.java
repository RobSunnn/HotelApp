package com.HotelApp.service;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.models.view.RoomView;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {

    void takeMoney(BigDecimal roomPrice);
//TODO: create guestView to get necessary info not directly from db
    List<GuestEntity> seeAllGuests();

    void makeAnotherUserAdmin();

    Long getCount();

    void init();

    List<RoomView> seeAllFreeRooms();

}
