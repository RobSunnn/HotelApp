package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ContactRequestBindingModel;

public interface ContactRequestService {
    void sendContactForm(ContactRequestBindingModel contactRequestBindingModel);

    void checkedContactRequest(Long id);

    void allRequestsChecked();
}
