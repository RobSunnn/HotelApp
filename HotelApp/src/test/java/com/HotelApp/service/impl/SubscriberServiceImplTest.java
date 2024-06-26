package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testAddNewSubscriber_withBindingErrors() {
        AddSubscriberBindingModel model = new AddSubscriberBindingModel();
        when(bindingResult.hasErrors()).thenReturn(true);

        subscriberService.addNewSubscriber(model, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("addSubscriberBindingModel", model);
        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.addSubscriberBindingModel", bindingResult);
        verify(redirectAttributes).addFlashAttribute("failMessage", "Please enter valid email.");
        verify(subscriberRepository, never()).findByEmail(anyString());
    }

    @Test
    void testAddNewSubscriber_existingSubscriber() {
        AddSubscriberBindingModel model = new AddSubscriberBindingModel();
        model.setSubscriberEmail("test@example.com");
        SubscriberEntity subscriber = new SubscriberEntity();
        subscriber.setEmail("test@example.com");
        subscriber.setCounterOfSubscriptions(1);
        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(subscriberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(subscriber));
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);

        subscriberService.addNewSubscriber(model, bindingResult, redirectAttributes);

        assertEquals(2, subscriber.getCounterOfSubscriptions());
        verify(subscriberRepository).save(subscriber);
        verify(redirectAttributes).addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");
    }

    @Test
    void testAddNewSubscriber_newSubscriber() {
        AddSubscriberBindingModel model = new AddSubscriberBindingModel();
        model.setSubscriberEmail("new@example.com");
        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        when(subscriberRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);

        subscriberService.addNewSubscriber(model, bindingResult, redirectAttributes);

        verify(subscriberRepository).save(any(SubscriberEntity.class));
        verify(redirectAttributes).addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");
    }


}