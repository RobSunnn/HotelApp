package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HappyGuestEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.AdminService;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.HappyGuestService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final HappyGuestService happyGuestService;

    private final AdminService adminService;

    private final ModelMapper modelMapper;

    private final ObservationRegistry observationRegistry;

    public GuestServiceImpl(GuestRepository guestRepository, RoomRepository roomRepository, HappyGuestService happyGuestService, AdminService adminService, ModelMapper modelMapper, ObservationRegistry observationRegistry) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.happyGuestService = happyGuestService;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel) {

        GuestEntity guest = guestRepository.save(map(addGuestBindingModel));

        RoomEntity room = roomRepository.findByRoomNumber(addGuestBindingModel.getRoomNumber()).setReserved(true);
        room.setReserved(true);
        roomRepository.save(room);
        Observation.createNotStarted("guestRegister", observationRegistry).observe(() -> guest);
        Observation.createNotStarted("roomRegister", observationRegistry).observe(() -> room);

        return guest.getDocumentId() != null;
    }

    @Override
    public void guestWantToLeave(Integer roomNumber) {
        GuestEntity guest = guestRepository.findByRoomNumber(roomNumber);
        RoomEntity room = roomRepository.findByRoomNumber(guest.getRoomNumber());

        adminService.takeMoney(room.getPrice());

        Optional<HappyGuestEntity> happyGuest = happyGuestService.findByDocumentId(guest);

        if (happyGuest.isPresent()) {
            happyGuest.get()
                    .setTimesThatGuestHaveBeenToHotel(happyGuest.get().getTimesThatGuestHaveBeenToHotel() + 1)
                    .setLastStay(LocalDate.now());

            happyGuestService.saveHappyGuest(happyGuest.get());
        } else {
            HappyGuestEntity happyGuestEntity = modelMapper.map(guest, HappyGuestEntity.class);
            happyGuestEntity.setLastRoomUsed(guest.getRoomNumber());
            happyGuestEntity.setTimesThatGuestHaveBeenToHotel(1);
            happyGuestEntity.setLastStay(LocalDate.now());
            happyGuestService.saveHappyGuest(happyGuestEntity);
        }
        Observation.createNotStarted("happyGuest", observationRegistry).observe(() -> happyGuest);
        guestRepository.delete(guest);

        room.setReserved(false);
        roomRepository.save(room);
    }

    @Override
    public List<GuestView> getAllGuests() {

        List<GuestEntity> allGuests = guestRepository.findAll();
        List<GuestView> allGuestsView = new ArrayList<>();

        for (GuestEntity guest : allGuests) {
            allGuestsView.add(map(guest));
        }

        return allGuestsView;
    }

    private GuestEntity map(AddGuestBindingModel addGuestBindingModel) {
        return new GuestEntity()
                .setFirstName(addGuestBindingModel.getFirstName())
                .setLastName(addGuestBindingModel.getLastName())
                .setAge(addGuestBindingModel.getAge())
                .setEmail(addGuestBindingModel.getEmail())
                .setDocumentId(addGuestBindingModel.getDocumentId())
                .setRoomNumber(addGuestBindingModel.getRoomNumber());
    }

    private GuestView map(GuestEntity guest) {
        return new GuestView()
                .setFirstName(guest.getFirstName())
                .setLastName(guest.getLastName())
                .setAge(guest.getAge())
                .setEmail(guest.getEmail())
                .setRoomNumber(guest.getRoomNumber())
                .setDocumentId(guest.getDocumentId());
    }

}
