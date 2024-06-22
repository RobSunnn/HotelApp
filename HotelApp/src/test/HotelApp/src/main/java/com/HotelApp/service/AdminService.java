package com.HotelApp.service;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.domain.models.view.RoomView;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {

    void takeMoney(BigDecimal roomPrice);

    List<GuestView> seeAllGuests();

    BigDecimal getTotalProfit();

    void makeAnotherUserAdmin();

    Long getCount();

    void init();

    List<RoomView> seeAllFreeRooms();

    List<UserEntity> findAllUsers();
}
