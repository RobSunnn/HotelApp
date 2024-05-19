package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.view.SubscriberView;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.SubscriberService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.HotelApp.config.SecurityConfiguration.modelMapper;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel, HotelInfoEntity hotelInfo) {

        Optional<SubscriberEntity> checkSubscriber = subscriberRepository.findByEmail(addSubscriberBindingModel.getSubscriberEmail());

        if (checkSubscriber.isPresent()) {
            SubscriberEntity subscriber = checkSubscriber.get();

            subscriber.setCounterOfSubscriptions(subscriber.getCounterOfSubscriptions() + 1);
            sendBonusVoucher();
            subscriberRepository.save(subscriber);
            return;
        }
        SubscriberEntity subscriber = subscriberRepository.save(mapAsSubscriber(addSubscriberBindingModel, hotelInfo));
        hotelInfo.getSubscribers().add(subscriber);

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
