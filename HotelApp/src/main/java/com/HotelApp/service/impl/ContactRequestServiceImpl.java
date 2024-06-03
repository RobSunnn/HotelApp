package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

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
    public void sendContactForm(ContactRequestBindingModel contactRequestBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("contactRequestBindingModel", contactRequestBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + "contactRequestBindingModel", bindingResult);

            return;
        }

        ContactRequestEntity contactRequest = modelMapper().map(contactRequestBindingModel, ContactRequestEntity.class);
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();

        contactRequest.setMessage(contactRequest.getMessage().trim());
        contactRequest.setChecked(false);
        contactRequest.setCreated(LocalDateTime.now());
        contactRequest.setHotelInfoEntity(hotelInfo);

        redirectAttributes.addFlashAttribute("successContactRequestMessage", "Contact Request Send, Thank You!");
        contactRequestRepository.save(contactRequest);
    }

    @Override
    public void checkedContactRequest(Long id) {
        contactRequestRepository
                .save(contactRequestRepository
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
