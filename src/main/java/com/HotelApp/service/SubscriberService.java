package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface SubscriberService {
    ResponseEntity<?> addNewSubscriber(AddSubscriberBindingModel subscriber, BindingResult bindingResult);
}
