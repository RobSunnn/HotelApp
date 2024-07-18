package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.events.SendBonusVoucherEvent;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.MailService;
import com.HotelApp.service.SubscriberService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private static final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);
    private final SubscriberRepository subscriberRepository;

    private final HotelService hotelService;

    private final EncryptionService encryptionService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MailService mailService;

    public SubscriberServiceImpl(
            SubscriberRepository subscriberRepository,
            HotelService hotelService,
            EncryptionService encryptionService,
            ApplicationEventPublisher applicationEventPublisher,
            MailService mailService
    ) {
        this.subscriberRepository = subscriberRepository;
        this.hotelService = hotelService;
        this.encryptionService = encryptionService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mailService = mailService;
    }


    @Override
    public boolean addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingConstants.SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.SUBSCRIBER_BINDING_MODEL, bindingResult);
            redirectAttributes.addFlashAttribute("failMessage", "Please enter valid email.");

            return false;
        }

        try {
            String decryptedEmail = encryptionService.decrypt(addSubscriberBindingModel.getSubscriberEmail());
            addSubscriberBindingModel.setSubscriberEmail(decryptedEmail);
            Optional<SubscriberEntity> checkSubscriber = subscriberRepository.findByEmail(decryptedEmail);
            HotelInfoEntity hotelInfo = hotelService.getHotelInfo();

            if (checkSubscriber.isPresent()) {
                SubscriberEntity subscriber = checkSubscriber.get();
                subscriber.setCounterOfSubscriptions(subscriber.getCounterOfSubscriptions() + 1);
                subscriberRepository.save(subscriber);

                if (subscriber.getCounterOfSubscriptions() == 2) {
                    applicationEventPublisher.publishEvent(new SendBonusVoucherEvent(
                            "SubscriberService", subscriber.getEmail())
                    );
                }
                return true;
            }

            subscriberRepository.save(mapAsSubscriber(addSubscriberBindingModel, hotelInfo));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @EventListener(SendBonusVoucherEvent.class)
    private void sendBonusVoucher(SendBonusVoucherEvent event) {
        mailService.sendBonusVoucherEmail(event.getEmail());
        log.info("Bonus voucher send for: {}", event.getEmail());
    }

    private static SubscriberEntity mapAsSubscriber(AddSubscriberBindingModel addSubscriberBindingModel, HotelInfoEntity hotelInfo) {
        return new SubscriberEntity()
                .setEmail(addSubscriberBindingModel.getSubscriberEmail())
                .setTimeOfSubscription(LocalDateTime.now())
                .setCounterOfSubscriptions(1)
                .setHotelInfoEntity(hotelInfo);
    }
}