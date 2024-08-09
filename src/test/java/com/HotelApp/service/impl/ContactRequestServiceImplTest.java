package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ContactRequestEntity;
import com.HotelApp.repository.ContactRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.HotelApp.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactRequestServiceImplTest {
    @Mock
    private ContactRequestRepository contactRequestRepository;

    @InjectMocks
    private ContactRequestServiceImpl contactRequestService;

    @Test
    void testClearCheckedContactRequests() {
        // Mock data
        List<ContactRequestEntity> mockRequests = new ArrayList<>();
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());
        mockRequests.add(mockContactRequestEntityWithCheckedTrue());

        when(contactRequestRepository.findAll()).thenReturn(mockRequests);

        contactRequestService.clearCheckedContactRequests();

        verify(contactRequestRepository, times(1)).findAll();
        verify(contactRequestRepository, times(1)).deleteAll(mockRequests);
    }

    @Test
    void testCheckedContactRequest() {
        ContactRequestEntity mockRequest = mockContactRequestEntityWithCheckedFalse();
        mockRequest.setId(1L);

        when(contactRequestRepository.findAll()).thenReturn(List.of(mockRequest));

        contactRequestService.checkedContactRequest(1L);

        verify(contactRequestRepository, times(1)).save(any(ContactRequestEntity.class));
        assertTrue(mockRequest.getChecked()); // Assert that isChecked was set to true
    }

    @Test
    void testCheckedContactRequest_NonExistingId() {
        assertThrows(RuntimeException.class, () -> contactRequestService.checkedContactRequest(1L));
    }

    @Test
    void testAllRequestsChecked() {
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
                .setName(TEST_NAME)
                .setChecked(true)
                .setMessage(TEST_MESSAGE)
                .setEmail(TEST_EMAIL)
                .setCreated(LocalDateTime.now());
    }

    private ContactRequestEntity mockContactRequestEntityWithCheckedFalse() {
        return new ContactRequestEntity()
                .setName(TEST_NAME)
                .setChecked(false)
                .setMessage(TEST_MESSAGE)
                .setEmail(TEST_EMAIL)
                .setCreated(LocalDateTime.now());
    }
}