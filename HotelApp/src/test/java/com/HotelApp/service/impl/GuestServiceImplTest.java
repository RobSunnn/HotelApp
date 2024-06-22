package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.*;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testRegisterGuest_withBindingErrors() {
        AddGuestBindingModel model = new AddGuestBindingModel();
        when(bindingResult.hasErrors()).thenReturn(true);

        boolean result = guestService.registerGuest(model, bindingResult, redirectAttributes);

        assertFalse(result);
        verify(redirectAttributes).addFlashAttribute("addGuestBindingModel", model);
        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.addGuestBindingModel", bindingResult);
    }

    @Test
    void testRegisterGuest_withoutBindingErrors() {
        AddGuestBindingModel model = mockAddGuestBindingModel();

        RoomEntity room = mockRoom();

        GuestEntity guestEntity = new GuestEntity();
        guestEntity.setDocumentId("123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(roomRepository.findByRoomNumber(1)).thenReturn(room);
        when(hotelService.getHotelInfo()).thenReturn(mockHotelInfoEntity());
        when(guestRepository.save(any(GuestEntity.class))).thenReturn(guestEntity);

        boolean result = guestService.registerGuest(model, bindingResult, redirectAttributes);

        assertTrue(result);
        verify(hotelService).takeMoney(BigDecimal.valueOf(3000));
        verify(roomRepository).save(room);
        assertTrue(room.isReserved());
        verify(guestRepository).save(any(GuestEntity.class));
    }

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
        when(happyGuestService.findByDocumentId("AbCde")).thenReturn(Optional.of(happyGuest));

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
        GuestEntity guest = new GuestEntity();
        guest.setDocumentId("123");
        guest.setRoom(room);
        guest.setCheckInTime(LocalDateTime.now().minusDays(2));
        HotelInfoEntity hotelInfo = new HotelInfoEntity();

        when(roomRepository.findByRoomNumber(roomNumber)).thenReturn(room);
        when(guestRepository.findByRoom(room)).thenReturn(guest);
        when(hotelService.getHotelInfo()).thenReturn(hotelInfo);
        when(happyGuestService.findByDocumentId("123")).thenReturn(Optional.empty());

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


    private AddGuestBindingModel mockAddGuestBindingModel() {
        return new AddGuestBindingModel()
                .setFirstName("Guest")
                .setLastName("BindingModel")
                .setEmail("guest@guest.bg")
                .setAge(33)
                .setDocumentId("ABC123")
                .setDaysToStay(3)
                .setRoomNumber(1);
    }

    private HotelInfoEntity mockHotelInfoEntity() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setName("Great Hotel");
        hotelInfo.setAddress("Somewhere");
        hotelInfo.setPhoneNumber("0987-654-321");
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
                .setFirstName("Guest")
                .setLastName("Entity")
                .setEmail("Guest@entity.bg")
                .setAge(33)
                .setCheckInTime(LocalDateTime.now().minusDays(2))
                .setDocumentId("AbCde")
                .setCheckOutTime(LocalDateTime.now())
                .setRoom(mockRoom());
    }

}