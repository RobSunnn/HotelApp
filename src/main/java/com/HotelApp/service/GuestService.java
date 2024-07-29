package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface GuestService {

    ResponseEntity<?> registerGuest(AddGuestBindingModel addGuestBindingModel, BindingResult bindingResult);

    void checkout(Integer roomNumber);

    List<GuestView> seeAllGuests();
}
