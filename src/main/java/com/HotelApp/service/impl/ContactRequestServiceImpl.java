package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.OnlineReservationEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.events.OnlineReservationEvent;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.repository.OnlineReservationRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.MailService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.HotelApp.common.constants.BindingConstants.CONTACT_REQUEST_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.*;
import static com.HotelApp.common.constants.SuccessConstants.ONLINE_RESERVATION_CONFIRMATION_MAIL_SEND;
import static com.HotelApp.common.constants.ValidationConstants.PHONE_NUMBER_TOO_LONG;
import static com.HotelApp.common.constants.ValidationConstants.TEXT_TOO_LONG;
import static com.HotelApp.util.ResponseUtil.genericFailResponse;
import static com.HotelApp.util.ResponseUtil.genericSuccessResponse;

@EnableScheduling
@Service
public class ContactRequestServiceImpl implements ContactRequestService {
    private static final int TEXT_MAXIMUM_LENGTH = 400;
    private static final String NO_ADDITIONAL_INFO = "No Additional Info.";
    private static final String SUCCESS_REDIRECT_URL = "/contact";
    private static final Logger log = LoggerFactory.getLogger(ContactRequestServiceImpl.class);

    private final ContactRequestRepository contactRequestRepository;
    private final OnlineReservationRepository onlineReservationRepository;
    private final HotelService hotelService;
    private final EncryptionService encryptionService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MailService mailService;

    public ContactRequestServiceImpl(
            ContactRequestRepository contactRequestRepository,
            OnlineReservationRepository onlineReservationRepository,
            HotelService hotelService,
            EncryptionService encryptionService,
            ApplicationEventPublisher applicationEventPublisher,
            MailService mailService
    ) {
        this.contactRequestRepository = contactRequestRepository;
        this.onlineReservationRepository = onlineReservationRepository;
        this.hotelService = hotelService;
        this.encryptionService = encryptionService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mailService = mailService;
    }

    @Override
    public ResponseEntity<?> sendContactForm(ContactRequestBindingModel contactRequestBindingModel, BindingResult bindingResult) {

        String decryptedEmail = encryptionService.decrypt(contactRequestBindingModel.getEmail());
        String decryptedPhone = encryptionService.decrypt(contactRequestBindingModel.getPhoneNumber());
        if (decryptedPhone.length() > 50) {
            bindingResult.addError(new FieldError(CONTACT_REQUEST_BINDING_MODEL,
                    "contactPhoneNumber", PHONE_NUMBER_TOO_LONG));
        }
        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
        }
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();
        ContactRequestEntity contactRequest = new ContactRequestEntity()
                .setName(contactRequestBindingModel.getName().trim())
                .setEmail(decryptedEmail)
                .setPhoneNumber(decryptedPhone)
                .setMessage(contactRequestBindingModel.getMessage().trim())
                .setChecked(false)
                .setCreated(LocalDateTime.now())
                .setHotelInfoEntity(hotelInfo);

        contactRequestRepository.save(contactRequest);
        log.info("You have a new contact request from: {}", contactRequestBindingModel.getEmail());
        return genericSuccessResponse(SUCCESS_REDIRECT_URL);
    }

    @Override
    public void checkedContactRequest(Long id) {
        contactRequestRepository.save(
                contactRequestRepository
                        .findAll()
                        .stream()
                        .filter(contactRequest -> Objects.equals(contactRequest.getId(), id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(NO_SUCH_REQUEST))
                        .setChecked(true)
        );
    }

    @Override
    public void allRequestsChecked() {
        contactRequestRepository
                .findAll()
                .stream()
                .filter(contactRequest -> !contactRequest.getChecked())
                .forEach(contactRequest -> contactRequestRepository.save(contactRequest.setChecked(true)));
    }

    @Override
    @Scheduled(cron = "@daily")
    public void clearCheckedContactRequests() {
        contactRequestRepository.deleteAll(
                contactRequestRepository
                        .findAll()
                        .stream()
                        .filter(ContactRequestEntity::getChecked)
                        .toList()
        );
    }

    @Transactional
    @Override
    public String makeOnlineReservation(String additionalInfo, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (additionalInfo.length() > TEXT_MAXIMUM_LENGTH) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, TEXT_TOO_LONG);
            return "redirect:/contact/onlineReservation";
        }

        UserEntity user = hotelService
                .getHotelInfo()
                .getUsers()
                .stream()
                .filter(userEntity -> userEntity.getEmail().equals(userEmail))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + userEmail + "not found!"));

        if (additionalInfo.isBlank()) {
            additionalInfo = NO_ADDITIONAL_INFO;
        }

        OnlineReservationEntity onlineReservationEntity = new OnlineReservationEntity()
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setAge(user.getAge())
                .setTimestamp(LocalDateTime.now())
                .setChecked(false)
                .setAdditionalInfo(additionalInfo)
                .setHotelInfoEntity(hotelService.getHotelInfo());

        onlineReservationRepository.save(onlineReservationEntity);

        applicationEventPublisher.publishEvent(
                new OnlineReservationEvent(
                        "OnlineReservation", user.getEmail(), user.getFullName()
                )
        );
        return "redirect:/contact/onlineReservationSuccess";
    }

    @Override
    public void checkedOnlineReservation(Long reservationId) {
        onlineReservationRepository.save(
                onlineReservationRepository
                        .findAll()
                        .stream()
                        .filter(onlineReservation -> onlineReservation.getId().equals(reservationId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(RESERVATION_NOT_FOUND))
                        .setChecked(true)
        );
    }

    @Override
    public void allReservationsChecked() {
        onlineReservationRepository
                .findAll()
                .stream()
                .filter(reservation -> !reservation.isChecked())
                .forEach(reservation -> onlineReservationRepository.save(reservation.setChecked(true)));
    }

    @Override
    @Scheduled(cron = "@monthly")
    public void clearCheckedReservations() {
        onlineReservationRepository.deleteAll(
                onlineReservationRepository
                        .findAll()
                        .stream()
                        .filter(OnlineReservationEntity::isChecked)
                        .toList()
        );
    }

    @EventListener(OnlineReservationEvent.class)
    protected void sendConfirmationEmailForOnlineReservation(OnlineReservationEvent event) {
        log.info(ONLINE_RESERVATION_CONFIRMATION_MAIL_SEND, event.getUserEmail());
        mailService.sendConfirmationEmailForOnlineReservation(event.getUserEmail(), event.getUserFullName());
    }
}
