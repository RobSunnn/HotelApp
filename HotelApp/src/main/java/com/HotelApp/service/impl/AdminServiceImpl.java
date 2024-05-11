package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.repository.AdminRepository;
import com.HotelApp.service.AdminService;
import com.HotelApp.service.UserService;
import com.HotelApp.service.helpers.GuestServiceHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final RoomServiceImpl roomService;

    private final GuestServiceHelper guestServiceHelper;

    private final UserService userService;

    public AdminServiceImpl(AdminRepository adminRepository,
                            RoomServiceImpl roomService,
                            GuestServiceHelper guestServiceHelper, UserService userService) {
        this.adminRepository = adminRepository;
        this.roomService = roomService;
        this.guestServiceHelper = guestServiceHelper;

        this.userService = userService;
    }

    @Override
    public void takeMoney(BigDecimal roomPrice) {
        HotelInfoEntity hotelInfo = getHotelInfo();

        hotelInfo.setTotalProfit(hotelInfo.getTotalProfit().add(roomPrice));
        adminRepository.save(hotelInfo);
    }

    @Override
    public List<RoomView> seeAllFreeRooms() {
        return roomService.getAvailableRooms();
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userService.findAllUsers();
    }

    @Override
    public List<GuestView> seeAllGuests() {
        return guestServiceHelper.getAllGuests();
    }

    @Override
    public BigDecimal getTotalProfit() {
        return getHotelInfo().getTotalProfit();
    }

    @Override
    public void makeAnotherUserAdmin() {

    }

    @Override
    public Long getCount() {
        return adminRepository.count();
    }

    @Override
    public void init() {
        adminRepository.save(new HotelInfoEntity(BigDecimal.ZERO));
    }

    private HotelInfoEntity getHotelInfo() {
        return adminRepository.findById(1L).orElseThrow();
    }

}
