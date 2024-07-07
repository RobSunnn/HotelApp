package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.OnlineReservationEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.repository.OnlineReservationRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@EnableScheduling
@Service
public class ContactRequestServiceImpl implements ContactRequestService {

    private static final Logger log = LoggerFactory.getLogger(ContactRequestServiceImpl.class);
    private final ContactRequestRepository contactRequestRepository;

    private final OnlineReservationRepository onlineReservationRepository;

    private final HotelService hotelService;

    private final EncryptionService encryptionService;

    public ContactRequestServiceImpl(ContactRequestRepository contactRequestRepository,
                                     OnlineReservationRepository onlineReservationRepository,
                                     HotelService hotelService,
                                     EncryptionService encryptionService) {
        this.contactRequestRepository = contactRequestRepository;
        this.onlineReservationRepository = onlineReservationRepository;
        this.hotelService = hotelService;
        this.encryptionService = encryptionService;
    }

    @Override
    public boolean sendContactForm(ContactRequestBindingModel contactRequestBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("contactRequestBindingModel", contactRequestBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + "contactRequestBindingModel", bindingResult);

            return false;
        }
        try {
            String decryptedEmail = encryptionService.decrypt(contactRequestBindingModel.getEmail());
            String decryptedPhone = encryptionService.decrypt(contactRequestBindingModel.getPhoneNumber());
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

            return true;

        } catch (Exception e) {
            log.warn("Failed to decrypt.");
            return false;
        }
    }

    @Override
    @Scheduled(cron = "@daily")
    public void clearCheckedContactRequests() {
        contactRequestRepository
                .deleteAll(contactRequestRepository.findAll()
                        .stream()
                        .filter(ContactRequestEntity::getChecked)
                        .toList());
    }

    @Transactional
    @Override
    public void makeOnlineReservation(String userEmail, String additionalInfo) {
        UserEntity user = hotelService
                .getHotelInfo()
                .getUsers()
                .stream()
                .filter(userView -> userView.getEmail().equals(userEmail))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + userEmail + "not found!"));

        OnlineReservationEntity onlineReservationEntity = new OnlineReservationEntity()
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setAge(user.getAge())
                .setTimestamp(LocalDateTime.now())
                .setChecked(false)
                .setHotelInfoEntity(hotelService.getHotelInfo());

        if (additionalInfo.equals("Max 400 symbols")) {
            additionalInfo = "No Additional Info.";
        }
        onlineReservationEntity.setAdditionalInfo(additionalInfo);

        onlineReservationRepository.save(onlineReservationEntity);
    }

    @Override
    public void checkedOnlineReservation(Long reservationId) {
        onlineReservationRepository.save(
                onlineReservationRepository
                        .findAll()
                        .stream()
                        .filter(onlineReservation -> onlineReservation.getId().equals(reservationId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Online Reservation not found."))
                        .setChecked(true)
        );
    }

    @Override
    public void checkedContactRequest(Long id) {
        contactRequestRepository.save(
                contactRequestRepository
                        .findAll()
                        .stream()
                        .filter(contactRequest -> Objects.equals(contactRequest.getId(), id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No such contact request."))
                        .setChecked(true));
    }

    @Override
    public void allRequestsChecked() {
        contactRequestRepository
                .findAll()
                .stream()
                .filter(contactRequest -> !contactRequest.getChecked())
                .forEach(contactRequest -> contactRequestRepository.save(contactRequest.setChecked(true)));
    }
}
