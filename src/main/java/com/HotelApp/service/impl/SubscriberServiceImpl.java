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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.HotelApp.common.constants.SuccessConstants.*;
import static com.HotelApp.common.constants.ValidationConstants.INVALID_EMAIL;
import static com.HotelApp.service.impl.HotelServiceImpl.genericFailResponse;
import static com.HotelApp.service.impl.HotelServiceImpl.genericSuccessResponse;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private static final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

    private final SubscriberRepository subscriberRepository;
    private final HotelService hotelService;
    private final EncryptionService encryptionService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MailService mailService;
    private final HttpServletRequest request;

    public SubscriberServiceImpl(
            SubscriberRepository subscriberRepository,
            HotelService hotelService,
            EncryptionService encryptionService,
            ApplicationEventPublisher applicationEventPublisher,
            MailService mailService,
            HttpServletRequest request
    ) {
        this.subscriberRepository = subscriberRepository;
        this.hotelService = hotelService;
        this.encryptionService = encryptionService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mailService = mailService;
        this.request = request;
    }


    @Override
    public ResponseEntity<?> addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel, BindingResult bindingResult) {
        final String REDIRECT_URL = request.getHeader("referer");

        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
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
                return genericSuccessResponse(REDIRECT_URL);
            }

            subscriberRepository.save(mapAsSubscriber(addSubscriberBindingModel, hotelInfo));
            return genericSuccessResponse(REDIRECT_URL);
        } catch (Exception e) {
            return genericFailResponse(bindingResult);
        }
    }

    @EventListener(SendBonusVoucherEvent.class)
    private void sendBonusVoucher(SendBonusVoucherEvent event) {
        mailService.sendBonusVoucherEmail(event.getEmail());
        log.info(BONUS_VOUCHER_SEND, event.getEmail());
    }

    private static SubscriberEntity mapAsSubscriber(
            AddSubscriberBindingModel addSubscriberBindingModel,
            HotelInfoEntity hotelInfo
    ) {
        return new SubscriberEntity()
                .setEmail(addSubscriberBindingModel.getSubscriberEmail())
                .setTimeOfSubscription(LocalDateTime.now())
                .setCounterOfSubscriptions(1)
                .setHotelInfoEntity(hotelInfo);
    }
}
