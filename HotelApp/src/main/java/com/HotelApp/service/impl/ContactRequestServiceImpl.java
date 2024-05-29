package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@Service
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;

    private final HotelService hotelService;

    public ContactRequestServiceImpl(ContactRequestRepository contactRequestRepository, HotelService hotelService) {
        this.contactRequestRepository = contactRequestRepository;
        this.hotelService = hotelService;
    }


    @Override
    public void sendContactForm(ContactRequestBindingModel contactRequestBindingModel) {
        ContactRequestEntity contactRequest = modelMapper().map(contactRequestBindingModel, ContactRequestEntity.class);
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();
        //TODO: Maybe we need to map it by hand not with model mapper to trim it correctly
        contactRequest.setMessage(contactRequest.getMessage().trim());
        contactRequest.setChecked(false);
        contactRequest.setCreated(LocalDateTime.now());
        contactRequest.setHotelInfoEntity(hotelInfo);

        contactRequestRepository.save(contactRequest);
    }
}
