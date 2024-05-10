package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.AdminService;
import com.HotelApp.service.GuestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    private final RoomRepository roomRepository;

    private final AdminService adminService;

    public GuestServiceImpl(GuestRepository guestRepository, RoomRepository roomRepository, AdminService adminService) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.adminService = adminService;
    }

    @Override
    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel) {

        GuestEntity guest = guestRepository.save(map(addGuestBindingModel));

        RoomEntity room = roomRepository.findByRoomNumber(addGuestBindingModel.getRoomNumber()).setReserved(true);
        room.setReserved(true);
        roomRepository.save(room);

        return guest.getDocumentId() != null;
    }

    @Override
    public void guestWantToLeave(Integer roomNumber) {
        GuestEntity guest = guestRepository.findByRoomNumber(roomNumber);
        RoomEntity room = roomRepository.findByRoomNumber(guest.getRoomNumber());

        adminService.takeMoney(room.getPrice());


        guestRepository.delete(guest);

        room.setReserved(false);
        roomRepository.save(room);

    }

    @Override
    public List<GuestEntity> getAllGuests() {
        return guestRepository.findAll();
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
}
