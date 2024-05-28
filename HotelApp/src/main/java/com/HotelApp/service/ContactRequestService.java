package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;

public interface ContactRequestService {
    void sendContactForm(ContactRequestBindingModel contactRequestBindingModel, HotelInfoEntity hotelInfo);
}
