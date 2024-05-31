package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;

import java.util.Objects;

public interface ContactRequestService {
    void sendContactForm(ContactRequestBindingModel contactRequestBindingModel);

    void checkedContactRequest(Long id);

    void allRequestsChecked();
}
