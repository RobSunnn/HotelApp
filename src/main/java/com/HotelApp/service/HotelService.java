package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.view.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HotelService {

    Long getCount();

    @Transactional
    void init();

    @Transactional(readOnly = true)
    HotelInfoEntity getHotelInfo();

    @Transactional
    void takeMoney(BigDecimal roomPrice);

    @Transactional(readOnly = true)
    List<RoomView> seeAllFreeRooms();

    @Transactional(readOnly = true)
    List<HappyGuestView> seeAllHappyGuests();

    @Transactional(readOnly = true)
    List<SubscriberView> seeAllSubscribers();

    @Transactional(readOnly = true)
    List<GuestView> seeAllGuests();

    @Transactional(readOnly = true)
    List<CommentView> getAllNotApprovedComments();

    @Transactional(readOnly = true)
    List<ContactRequestView> getAllNotCheckedContactRequest();


    @Transactional(readOnly = true)
    BigDecimal getTotalProfit();

    @Transactional(readOnly = true)
    Map<String, Integer> getInfoForHotel();

    @Transactional(readOnly = true)
    List<UserView> findAllUsers();

    @Transactional(readOnly = true)
    List<OnlineReservationView> getAllNotCheckedOnlineReservations();
}
