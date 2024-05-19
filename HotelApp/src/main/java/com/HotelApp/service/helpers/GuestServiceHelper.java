//package com.HotelApp.service.helpers;
//
//import com.HotelApp.domain.entity.GuestEntity;
//import com.HotelApp.domain.models.binding.AddGuestBindingModel;
//import com.HotelApp.domain.models.view.GuestView;
//import com.HotelApp.repository.GuestRepository;
//import com.HotelApp.service.GuestService;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class GuestServiceHelper implements GuestService {
//
//    private final GuestRepository guestRepository;
//
//    public GuestServiceHelper(GuestRepository guestRepository) {
//        this.guestRepository = guestRepository;
//    }
//
//    @Override
//    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel) {
//        return false;
//    }
//
//    @Override
//    public void guestWantToLeave(Integer roomNumber) {
//
//    }
//
//    @Override
//    public List<GuestView> getAllGuests() {
//        List<GuestEntity> allGuests = guestRepository.findAll();
//        List<GuestView> allGuestsView = new ArrayList<>();
//
//        for (GuestEntity guest : allGuests) {
//            allGuestsView.add(map(guest));
//        }
//
//        return allGuestsView;
//    }
//
//    private GuestView map(GuestEntity guest) {
//        return new GuestView()
//                .setFirstName(guest.getFirstName())
//                .setLastName(guest.getLastName())
//                .setAge(guest.getAge())
//                .setEmail(guest.getEmail())
//                .setRoomNumber(guest.getRoom().getRoomNumber())
//                .setDocumentId(guest.getDocumentId());
//    }
//}
