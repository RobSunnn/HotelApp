package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ContactRequestService {

    boolean sendContactForm(ContactRequestBindingModel contactRequestBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    void checkedContactRequest(Long id);

    void allRequestsChecked();

    void clearCheckedContactRequests();
}
