package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.view.*;
import com.HotelApp.repository.HotelRepository;
import com.HotelApp.service.HotelService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;


@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Long getCount() {
        return hotelRepository.count();
    }

    @PostConstruct
    @Transactional
    @Override
    public void init() {
        if (hotelRepository.count() == 0) {
            HotelInfoEntity hotelInfo = new HotelInfoEntity();
            hotelInfo.setName("Great Hotel");
            hotelInfo.setAddress("Somewhere");
            hotelInfo.setPhoneNumber("0987-654-321");
            hotelInfo.setTotalProfit(BigDecimal.ZERO);

            hotelRepository.save(hotelInfo);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public HotelInfoEntity getHotelInfo() {
        return hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException("Hotel Info not found"));
    }

    @Transactional
    @Override
    public void takeMoney(BigDecimal vacationPrice) {
        HotelInfoEntity hotelInfo = getHotelInfo();

        hotelInfo.setTotalProfit(hotelInfo.getTotalProfit().add(vacationPrice));
        hotelRepository.save(hotelInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomView> seeAllFreeRooms() {
        return getHotelInfo()
                .getRooms()
                .stream()
                .filter(room -> !room.isReserved())
                .map(room -> modelMapper().map(room, RoomView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<HappyGuestView> seeAllHappyGuests() {
        return getHotelInfo()
                .getHappyGuests()
                .stream()
                .map(happyGuestEntity -> modelMapper().map(happyGuestEntity, HappyGuestView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscriberView> seeAllSubscribers() {
        return getHotelInfo()
                .getSubscribers()
                .stream()
                .map(subscriberEntity -> modelMapper().map(subscriberEntity, SubscriberView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<GuestView> seeAllGuests() {
        return getHotelInfo()
                .getGuests()
                .stream()
                .map(guest -> modelMapper().map(guest, GuestView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentView> getAllNotApprovedComments() {
        return getHotelInfo()
                .getComments()
                .stream()
                .filter(comment -> !comment.getApproved())
                .map(comment -> modelMapper().map(comment, CommentView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContactRequestView> getAllNotCheckedContactRequest() {
        return getHotelInfo()
                .getContactRequests()
                .stream()
                .filter(contactRequest -> !contactRequest.getChecked())
                .map(contactRequest -> modelMapper().map(contactRequest, ContactRequestView.class))
                .toList();
    }

    @Transactional
    @Override
    public BigDecimal getTotalProfit() {
        return getHotelInfo().getTotalProfit();
    }

}
