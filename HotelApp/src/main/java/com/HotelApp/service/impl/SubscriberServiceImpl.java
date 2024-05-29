package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.SubscriberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;

    private final HotelService hotelService;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, HotelService hotelService) {
        this.subscriberRepository = subscriberRepository;
        this.hotelService = hotelService;
    }

    @Override
    public void addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel) {

        Optional<SubscriberEntity> checkSubscriber = subscriberRepository.findByEmail(addSubscriberBindingModel.getSubscriberEmail());
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();
        if (checkSubscriber.isPresent()) {
            SubscriberEntity subscriber = checkSubscriber.get();
            subscriber.setCounterOfSubscriptions(subscriber.getCounterOfSubscriptions() + 1);
            sendBonusVoucher();
            subscriberRepository.save(subscriber);

            return;
        }
        subscriberRepository.save(mapAsSubscriber(addSubscriberBindingModel, hotelInfo));
    }

    private void sendBonusVoucher() {

        //todo: send bonus
    }

    private static SubscriberEntity mapAsSubscriber(AddSubscriberBindingModel addSubscriberBindingModel, HotelInfoEntity hotelInfo) {
        return new SubscriberEntity()
                .setEmail(addSubscriberBindingModel.getSubscriberEmail())
                .setTimeOfSubscription(LocalDateTime.now())
                .setCounterOfSubscriptions(1)
                .setHotelInfoEntity(hotelInfo);
    }
}
