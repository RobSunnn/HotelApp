package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.service.HotelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactRequestServiceImplTest {

    @Mock
    private ContactRequestRepository contactRequestRepository;

    @Mock
    private HotelService hotelService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ContactRequestServiceImpl contactRequestService;

    @Test
    public void testSendContactForm_Success() {
        // Mock data
        ContactRequestBindingModel bindingModel = new ContactRequestBindingModel();
        bindingModel.setName("John Doe");
        bindingModel.setEmail("john.doe@example.com");
        bindingModel.setMessage("Test message");

        HotelInfoEntity mockHotelInfo = new HotelInfoEntity();
        when(hotelService.getHotelInfo()).thenReturn(mockHotelInfo);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ContactRequestEntity savedEntity = new ContactRequestEntity();
        when(contactRequestRepository.save(any(ContactRequestEntity.class))).thenReturn(savedEntity);

        // Execute the method
        contactRequestService.sendContactForm(bindingModel, bindingResult, redirectAttributes);

        // Verify interactions and assertions
        verify(hotelService, times(1)).getHotelInfo();
        verify(contactRequestRepository, times(1)).save(any(ContactRequestEntity.class));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successContactRequestMessage"), eq("Contact Request Send, Thank You!"));
    }

    @Test
    public void testSendContactForm_WithErrors() {
        // Mock data with errors
        ContactRequestBindingModel bindingModel = new ContactRequestBindingModel();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Execute the method
        contactRequestService.sendContactForm(bindingModel, bindingResult, redirectAttributes);

        // Verify interactions and assertions
        verify(bindingResult, times(1)).hasErrors();
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("contactRequestBindingModel"), eq(bindingModel));
    }

    @Test
    public void testClearCheckedContactRequests() {
        // Mock data
        List<ContactRequestEntity> mockRequests = new ArrayList<>();
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());

        when(contactRequestRepository.findAll()).thenReturn(mockRequests);

        // Execute the method
        contactRequestService.clearCheckedContactRequests();

        // Verify interactions and assertions
        verify(contactRequestRepository, times(1)).findAll();
        verify(contactRequestRepository, times(1)).deleteAll(mockRequests);

    }

//    @Test
//    public void testCheckedContactRequest() {
//        // Mock data
//        ContactRequestEntity mockRequest = mockContactRequestEntityWithCheckedFalse();
//        mockRequest.setId(1L); // Set an ID that matches the ID we expect in the service method
//        when(contactRequestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));
//
//        // Execute the method
//        contactRequestService.checkedContactRequest(1L);
//
//        // Verify interactions and assertions
//        verify(contactRequestRepository, times(1)).findById(1L);
//        verify(contactRequestRepository, times(1)).save(any(ContactRequestEntity.class));
//        // Additional verification if needed
//        assertTrue(mockRequest.getChecked()); // Assert that isChecked was set to true
//    }

    @Test
    public void testCheckedContactRequest_NonExistingId() {
        // Execute and assert exception thrown
        assertThrows(RuntimeException.class, () -> contactRequestService.checkedContactRequest(1L));
    }

    @Test
    public void testAllRequestsChecked() {
        // Mock data
        List<ContactRequestEntity> mockRequests = new ArrayList<>();
        mockRequests.add(mockContactRequestEntityWithCheckedFalse());
        when(contactRequestRepository.findAll()).thenReturn(mockRequests);

        // Execute the method
        contactRequestService.allRequestsChecked();

        // Verify interactions and assertions
        verify(contactRequestRepository, times(1)).findAll();
        verify(contactRequestRepository, times(mockRequests.size())).save(any(ContactRequestEntity.class));
    }

    private ContactRequestEntity mockContactRequestEntityWithCheckedTrue() {
        return new ContactRequestEntity()
                .setName("name")
                .setChecked(true)
                .setMessage("test")
                .setEmail("test@test.bg")
                .setCreated(LocalDateTime.now());
    }

    private ContactRequestEntity mockContactRequestEntityWithCheckedFalse() {
        return new ContactRequestEntity()
                .setName("name")
                .setChecked(false)
                .setMessage("test")
                .setEmail("test@test.bg")
                .setCreated(LocalDateTime.now());
    }
}