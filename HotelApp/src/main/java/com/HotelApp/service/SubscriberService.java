package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;

public interface SubscriberService {
    void addNewSubscriber(AddSubscriberBindingModel subscriber, HotelInfoEntity hotelInfo);

}
