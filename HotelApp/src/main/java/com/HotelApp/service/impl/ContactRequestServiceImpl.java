package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.service.ContactRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@Service
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;

    public ContactRequestServiceImpl(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
    }


    @Override
    public void sendContactForm(ContactRequestBindingModel contactRequestBindingModel, HotelInfoEntity hotelInfo) {
        ContactRequestEntity contactRequest = modelMapper().map(contactRequestBindingModel, ContactRequestEntity.class);
        contactRequest.setChecked(false);
        contactRequest.setCreated(LocalDateTime.now());
        contactRequest.setHotelInfoEntity(hotelInfo);

        contactRequestRepository.save(contactRequest);
    }
}
