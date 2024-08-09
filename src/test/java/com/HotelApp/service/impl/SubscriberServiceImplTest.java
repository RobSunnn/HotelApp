package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.HotelService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

import static com.HotelApp.common.constants.SuccessConstants.*;
import static com.HotelApp.constants.FieldConstants.DECRYPTED_EMAIL_FIELD;
import static com.HotelApp.constants.FieldConstants.ENCRYPTED_EMAIL_FIELD;
import static com.HotelApp.constants.TestConstants.*;
import static com.HotelApp.constants.urlsAndViewsConstants.TEST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private HttpServletRequest request;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setUp() {
        when(request.getHeader(REFERER)).thenReturn(TEST_URL);
    }

    @Test
    void testAddNewSubscriber() {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel();
        addSubscriberBindingModel.setSubscriberEmail(ENCRYPTED_EMAIL_FIELD);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(encryptionService.decrypt(ENCRYPTED_EMAIL_FIELD)).thenReturn(DECRYPTED_EMAIL_FIELD);
        when(subscriberRepository.findByEmail(DECRYPTED_EMAIL_FIELD)).thenReturn(Optional.empty());

        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);

        ResponseEntity<?> response = subscriberService.addNewSubscriber(addSubscriberBindingModel, bindingResult);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get(SUCCESS));
        assertEquals(TEST_URL, responseBody.get(REDIRECT_URL));
        verify(subscriberRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testAddNewSubscriber_withBindingErrors() {
        AddSubscriberBindingModel model = new AddSubscriberBindingModel();
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = subscriberService.addNewSubscriber(model, bindingResult);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertNotNull(responseBody);
        assertEquals(false, responseBody.get(SUCCESS));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(subscriberRepository, never()).findByEmail(anyString());
    }

    @Test
    void testAddNewSubscriber_existingSubscriber() {
        AddSubscriberBindingModel model = new AddSubscriberBindingModel();
        model.setSubscriberEmail(TEST_EMAIL);
        SubscriberEntity subscriber = new SubscriberEntity()
                .setEmail(TEST_EMAIL)
                .setCounterOfSubscriptions(1);

        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(encryptionService.decrypt(model.getSubscriberEmail())).thenReturn(TEST_EMAIL);
        when(subscriberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(subscriber));
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);

        ResponseEntity<?> response = subscriberService.addNewSubscriber(model, bindingResult);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get(SUCCESS));
        assertEquals(TEST_URL, responseBody.get(REDIRECT_URL));
        verify(subscriberRepository, times(1)).findByEmail(anyString());
        assertEquals(2, subscriber.getCounterOfSubscriptions());
        verify(subscriberRepository).save(subscriber);
    }
}