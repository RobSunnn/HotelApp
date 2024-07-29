package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ContactRequestService {

    ResponseEntity<?> sendContactForm(ContactRequestBindingModel contactRequestBindingModel, BindingResult bindingResult);

    void checkedContactRequest(Long id);

    void allRequestsChecked();

    void clearCheckedContactRequests();

    String makeOnlineReservation(String additionalInfo, RedirectAttributes redirectAttributes);

    void checkedOnlineReservation(Long reservationId);

}
