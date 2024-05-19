package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;

import java.util.List;

public interface GuestService {

    boolean registerGuest(AddGuestBindingModel addGuestBindingModel, HotelInfoEntity hotelInfo);

    void guestWantToLeave(RoomEntity room, HotelInfoEntity hotelInfo);

}
