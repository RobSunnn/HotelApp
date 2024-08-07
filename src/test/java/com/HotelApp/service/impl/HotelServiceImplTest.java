package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.*;
import com.HotelApp.domain.models.view.*;
import com.HotelApp.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.common.constants.ValidationConstants.HOTEL_INFO_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {
    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void testGetCount() {
        when(hotelRepository.count()).thenReturn(1L);

        Long count = hotelService.getCount();

        assertEquals(1L, count);
        verify(hotelRepository).count();
    }

    @Test
    void testInit_whenCountIsZero() {
        when(hotelRepository.count()).thenReturn(0L);

        hotelService.init();

        verify(hotelRepository).save(any(HotelInfoEntity.class));
    }

    @Test
    void testInit_whenCountIsNotZero() {
        when(hotelRepository.count()).thenReturn(1L);

        hotelService.init();

        verify(hotelRepository, never()).save(any(HotelInfoEntity.class));
    }

    @Test
    void testGetHotelInfo() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        HotelInfoEntity result = hotelService.getHotelInfo();

        assertEquals(hotelInfo, result);
        verify(hotelRepository).findById(1L);
    }

    @Test
    void testGetHotelInfo_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hotelService.getHotelInfo();
        });

        assertEquals(HOTEL_INFO_NOT_FOUND, exception.getMessage());
        verify(hotelRepository).findById(1L);
    }

    @Test
    void testTakeMoney() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setTotalProfit(BigDecimal.valueOf(100));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        hotelService.takeMoney(BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), hotelInfo.getTotalProfit());
        verify(hotelRepository).save(hotelInfo);
    }

    @Test
    void testSeeAllFreeRooms() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        RoomEntity freeRoom = new RoomEntity();
        freeRoom.setReserved(false);
        RoomEntity reservedRoom = new RoomEntity();
        reservedRoom.setReserved(true);

        hotelInfo.setRooms(List.of(freeRoom, reservedRoom));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<RoomView> result = hotelService.seeAllFreeRooms();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isReserved());
    }

    @Test
    void testSeeAllHappyGuests() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        HappyGuestEntity happyGuest = new HappyGuestEntity();
        hotelInfo.setHappyGuests(List.of(happyGuest));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<HappyGuestView> result = hotelService.seeAllHappyGuests();

        assertEquals(1, result.size());
    }

    @Test
    void testSeeAllSubscribers() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        SubscriberEntity subscriber = new SubscriberEntity();
        hotelInfo.setSubscribers(List.of(subscriber));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<SubscriberView> result = hotelService.seeAllSubscribers();

        assertEquals(1, result.size());
    }

    @Test
    void testSeeAllGuests() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        GuestEntity guest = new GuestEntity();
        GuestEntity guest1 = new GuestEntity();
        GuestEntity guest2 = new GuestEntity();
        hotelInfo.setGuests(List.of(guest, guest1, guest2));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<GuestView> result = hotelService.seeAllGuests();

        assertEquals(3, result.size());
    }

    @Test
    void testGetAllNotApprovedComments() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        CommentEntity approvedComment = new CommentEntity();
        approvedComment.setApproved(true);
        CommentEntity notApprovedComment = new CommentEntity();
        notApprovedComment.setApproved(false);
        hotelInfo.setComments(List.of(approvedComment, notApprovedComment));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<CommentView> result = hotelService.getAllNotApprovedComments();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getApproved());
    }

    @Test
    void testGetAllNotCheckedContactRequest() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        ContactRequestEntity checkedRequest = new ContactRequestEntity();
        checkedRequest.setChecked(true);
        ContactRequestEntity notCheckedRequest = new ContactRequestEntity();
        notCheckedRequest.setChecked(false);
        hotelInfo.setContactRequests(List.of(checkedRequest, notCheckedRequest));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        List<ContactRequestView> result = hotelService.getAllNotCheckedContactRequest();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getChecked());
    }

    @Test
    void testGetTotalProfit() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setTotalProfit(BigDecimal.valueOf(1000));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelInfo));

        BigDecimal result = hotelService.getTotalProfit();

        assertEquals(BigDecimal.valueOf(1000), result);
    }
}