package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.*;
import com.HotelApp.domain.models.view.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface HotelService {

    Long getCount();

    void init();

    HotelInfoEntity getHotelInfo();

    void takeMoney(BigDecimal roomPrice);

    List<RoomView> seeAllFreeRooms();

    List<HappyGuestView> seeAllHappyGuests();

    List<SubscriberView> seeAllSubscribers();

    List<GuestView> seeAllGuests();

    List<CommentView> getAllNotApprovedComments();

    List<ContactRequestView> getAllNotCheckedContactRequest();

    void checkedContactRequest(Long id);

    BigDecimal getTotalProfit();

}
