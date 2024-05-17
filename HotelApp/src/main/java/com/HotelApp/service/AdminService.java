package com.HotelApp.service;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.domain.models.view.SubscriberView;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {

    void takeMoney(BigDecimal roomPrice);

    List<GuestView> seeAllGuests();

    BigDecimal getTotalProfit();

    Long getCount();

    void init();

    List<RoomView> seeAllFreeRooms();

    List<SubscriberView> seeAllSubscribers();
}
