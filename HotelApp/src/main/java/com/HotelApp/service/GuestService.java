package com.HotelApp.service;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;

import java.util.List;

public interface GuestService {

    boolean registerGuest(AddGuestBindingModel addGuestBindingModel);

    void guestWantToLeave(Integer roomNumber);

    List<GuestEntity> getAllGuests();
}
