package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.view.SubscriberView;

import java.util.List;

public interface SubscriberService {

    void addNewSubscriber(AddSubscriberBindingModel subscriber, HotelInfoEntity hotelInfo);

}
