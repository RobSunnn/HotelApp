package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HappyGuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.HappyGuestService;
import com.HotelApp.service.HotelService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.common.constants.BindingConstants.GUEST_REGISTER_BINDING_MODEL;
import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final HappyGuestService happyGuestService;
    private final HotelService hotelService;
    private final EncryptionService encryptionService;

    public GuestServiceImpl(GuestRepository guestRepository,
                            RoomRepository roomRepository,
                            HappyGuestService happyGuestService, HotelService hotelService, EncryptionService encryptionService) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.happyGuestService = happyGuestService;
        this.hotelService = hotelService;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional
    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        try {
            String decryptedEmail = encryptionService.decrypt(addGuestBindingModel.getEmail());

            String decryptedDocument = encryptionService.decrypt(addGuestBindingModel.getDocumentId());
            if (decryptedDocument.isEmpty()) {
                bindingResult.addError(new FieldError(GUEST_REGISTER_BINDING_MODEL,
                        "documentId", "Document ID should not be empty!"));
            }
            addGuestBindingModel.setEmail(decryptedEmail);
            addGuestBindingModel.setDocumentId(decryptedDocument);

            if (bindingResult.hasErrors()) {
                redirectAttributes
                        .addFlashAttribute(GUEST_REGISTER_BINDING_MODEL, addGuestBindingModel);
                redirectAttributes
                        .addFlashAttribute(BindingConstants.BINDING_RESULT_PATH
                                + GUEST_REGISTER_BINDING_MODEL, bindingResult);

                return false;
            }

            RoomEntity room = roomRepository.findByRoomNumber(addGuestBindingModel.getRoomNumber());
            HotelInfoEntity hotelInfo = hotelService.getHotelInfo();

            hotelService.takeMoney(room.getPrice().multiply(BigDecimal.valueOf(addGuestBindingModel.getDaysToStay())));
            room.setReserved(true);
            roomRepository.save(room);
            GuestEntity guest = guestRepository.save(mapAsGuest(addGuestBindingModel, hotelInfo));

            return guest.getDocumentId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void checkout(Integer roomNumber) {
        RoomEntity room = roomRepository.findByRoomNumber(roomNumber);
        room.setReserved(false);

        GuestEntity guest = guestRepository.findByRoom(room);
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();

        Optional<HappyGuestEntity> happyGuest = happyGuestService.findByDocumentId(guest.getDocumentId());

        if (happyGuest.isPresent()) {
            HappyGuestEntity happyGuestEntity = happyGuest.get();

            happyGuestEntity
                    .setTimesThatGuestHaveBeenToHotel(happyGuestEntity.getTimesThatGuestHaveBeenToHotel() + 1)
                    .setLastCheckIn(guest.getCheckInTime())
                    .setLastCheckOut(LocalDateTime.now());

            happyGuestService.saveHappyGuest(happyGuest.get());
        } else {
            HappyGuestEntity happyGuestEntity = modelMapper().map(guest, HappyGuestEntity.class)
                    .setLastRoomUsed(guest.getRoom().getRoomNumber())
                    .setTimesThatGuestHaveBeenToHotel(1)
                    .setLastCheckIn(guest.getCheckInTime())
                    .setLastCheckOut(LocalDateTime.now())
                    .setHotelInfoEntity(hotelInfo);

            happyGuestService.saveHappyGuest(happyGuestEntity);
        }

        guestRepository.delete(guest);
        roomRepository.save(room);
    }

    @Override
    public List<GuestView> seeAllGuests() {
        return guestRepository
                .findAll()
                .stream()
                .map(guest -> modelMapper().map(guest, GuestView.class))
                .toList();
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
