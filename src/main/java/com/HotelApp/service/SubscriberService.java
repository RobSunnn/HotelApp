package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface SubscriberService {
    ResponseEntity<?> addNewSubscriber(AddSubscriberBindingModel subscriber, BindingResult bindingResult);
}
