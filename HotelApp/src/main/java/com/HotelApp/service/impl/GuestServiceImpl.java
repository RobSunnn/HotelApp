package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HappyGuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.HappyGuestService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@Service
@Primary
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final HappyGuestService happyGuestService;

    public GuestServiceImpl(GuestRepository guestRepository,
                            RoomRepository roomRepository,
                            HappyGuestService happyGuestService) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.happyGuestService = happyGuestService;
    }

    @Override
    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel, HotelInfoEntity hotelInfo) {
        GuestEntity guest = guestRepository.save(mapAsGuest(addGuestBindingModel, hotelInfo));
        hotelInfo.getGuests().add(guest);

        return guest.getDocumentId() != null;
    }

    @Override
    public void guestWantToLeave(RoomEntity room, HotelInfoEntity hotelInfo) {

        GuestEntity guest = guestRepository.findByRoom(room);

        Optional<HappyGuestEntity> happyGuest = happyGuestService.findByDocumentId(guest.getDocumentId());

        if (happyGuest.isPresent()) {
            HappyGuestEntity happyGuestEntity = happyGuest.get();

            happyGuestEntity
                    .setTimesThatGuestHaveBeenToHotel(happyGuestEntity.getTimesThatGuestHaveBeenToHotel() + 1)
                    .setLastCheckIn(guest.getCheckInTime())
                    .setLastCheckOut(LocalDateTime.now());

            hotelInfo.getHappyGuests().add(happyGuestEntity);

            happyGuestService.saveHappyGuest(happyGuest.get());
        } else {
            HappyGuestEntity happyGuestEntity = modelMapper().map(guest, HappyGuestEntity.class);

            happyGuestEntity.setLastRoomUsed(guest.getRoom().getRoomNumber());
            happyGuestEntity.setTimesThatGuestHaveBeenToHotel(1);
            happyGuestEntity.setLastCheckIn(guest.getCheckInTime());
            happyGuestEntity.setLastCheckOut(LocalDateTime.now());
            happyGuestEntity.setHotelInfoEntity(hotelInfo);

            hotelInfo.getHappyGuests().add(happyGuestEntity);
            happyGuestService.saveHappyGuest(happyGuestEntity);
        }
        hotelInfo.getGuests().remove(guest);

        guestRepository.delete(guest);
        roomRepository.save(room);
    }

    private GuestEntity mapAsGuest(AddGuestBindingModel addGuestBindingModel, HotelInfoEntity hotelInfo) {
        RoomEntity room = roomRepository.findByRoomNumber(addGuestBindingModel.getRoomNumber());

        return new GuestEntity()
                .setFirstName(addGuestBindingModel.getFirstName())
                .setLastName(addGuestBindingModel.getLastName())
                .setAge(addGuestBindingModel.getAge())
                .setEmail(addGuestBindingModel.getEmail())
                .setDocumentId(addGuestBindingModel.getDocumentId())
                .setRoom(room)
                .setCheckInTime(LocalDateTime.now())
                .setCheckOutTime(LocalDateTime.now().plusDays(addGuestBindingModel.getDaysToStay()))
                .setHotelInfoEntity(hotelInfo);
    }

}
