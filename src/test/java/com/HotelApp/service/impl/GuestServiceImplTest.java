package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HappyGuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.HappyGuestService;
import com.HotelApp.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.common.constants.AppConstants.*;
import static com.HotelApp.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HappyGuestService happyGuestService;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    void testCheckout_existingHappyGuest() {
        int roomNumber = 1;
        RoomEntity room = mockRoom();
        room.setReserved(true);

        GuestEntity guest = mockGuest();
        HotelInfoEntity hotelInfo = mockHotelInfoEntity();
        HappyGuestEntity happyGuest = new HappyGuestEntity();
        happyGuest.setTimesThatGuestHaveBeenToHotel(2);

        when(roomRepository.findByRoomNumber(roomNumber)).thenReturn(room);
        when(guestRepository.findByRoom(room)).thenReturn(guest);
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);
        when(happyGuestService.findByDocumentId(MOCK_GUEST_DOCUMENT)).thenReturn(Optional.of(happyGuest));

        guestService.checkout(roomNumber);

        assertFalse(room.isReserved());
        verify(roomRepository).save(room);
        verify(guestRepository).delete(guest);
        verify(happyGuestService).saveHappyGuest(happyGuest);
    }

    @Test
    void testCheckout_newHappyGuest() {
        int roomNumber = 1;
        RoomEntity room = new RoomEntity();
        GuestEntity guest = mockGuest();
        guest.setCheckInTime(LocalDateTime.now().minusDays(2));
        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        when(roomRepository.findByRoomNumber(roomNumber)).thenReturn(room);
        when(guestRepository.findByRoom(room)).thenReturn(guest);
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);
        when(happyGuestService.findByDocumentId(MOCK_GUEST_DOCUMENT)).thenReturn(Optional.empty());

        guestService.checkout(roomNumber);

        assertFalse(room.isReserved());
        verify(roomRepository).save(room);
        verify(guestRepository).delete(guest);
        verify(happyGuestService).saveHappyGuest(any(HappyGuestEntity.class));
    }

    @Test
    void testSeeAllGuests() {
        GuestEntity guest1 = new GuestEntity();
        GuestEntity guest2 = new GuestEntity();
        when(guestRepository.findAll()).thenReturn(List.of(guest1, guest2));

        List<GuestView> guests = guestService.seeAllGuests();

        assertEquals(2, guests.size());
        verify(guestRepository).findAll();
    }

    private HotelInfoEntity mockHotelInfoEntity() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setName(HOTEL_NAME);
        hotelInfo.setAddress(HOTEL_ADDRESS);
        hotelInfo.setPhoneNumber(HOTEL_PHONE);
        hotelInfo.setTotalProfit(BigDecimal.ZERO);
        return hotelInfo;
    }

    private RoomEntity mockRoom() {
        return new RoomEntity()
                .setRoomNumber(1)
                .setReserved(false)
                .setPrice(BigDecimal.valueOf(1000));
    }

    private GuestEntity mockGuest() {
        return new GuestEntity()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setEmail(TEST_EMAIL)
                .setAge(33)
                .setCheckInTime(LocalDateTime.now().minusDays(2))
                .setDocumentId(MOCK_GUEST_DOCUMENT)
                .setCheckOutTime(LocalDateTime.now())
                .setRoom(mockRoom());
    }

}