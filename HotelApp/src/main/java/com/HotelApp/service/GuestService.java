package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface GuestService {

    boolean registerGuest(AddGuestBindingModel addGuestBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    void checkout(Integer roomNumber);

    List<GuestView> seeAllGuests();

}
