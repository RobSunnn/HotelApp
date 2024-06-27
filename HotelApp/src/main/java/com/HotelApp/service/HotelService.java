package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.*;
import com.HotelApp.domain.models.view.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HotelService {

    Long getCount();

    @Transactional
    void init();

    @Transactional
    HotelInfoEntity getHotelInfo();

    @Transactional
    void takeMoney(BigDecimal roomPrice);

    @Transactional
    List<RoomView> seeAllFreeRooms();

    @Transactional
    List<HappyGuestView> seeAllHappyGuests();

    @Transactional
    List<SubscriberView> seeAllSubscribers();

    @Transactional
    List<GuestView> seeAllGuests();

    @Transactional
    List<CommentView> getAllNotApprovedComments();

    @Transactional
    List<ContactRequestView> getAllNotCheckedContactRequest();

    @Transactional
    BigDecimal getTotalProfit();

    @Transactional
    Map<String, Integer> getInfoForHotel();
}
