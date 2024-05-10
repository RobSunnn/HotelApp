package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.repository.AdminRepository;
import com.HotelApp.service.AdminService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void takeMoney(BigDecimal roomPrice) {
        HotelInfoEntity hotelInfo = adminRepository.findById(1L).orElseThrow();

        hotelInfo.setTotalProfit(hotelInfo.getTotalProfit().add(roomPrice));
        adminRepository.save(hotelInfo);
    }

    @Override
    public List<GuestEntity> seeAllGuests() {
        return null;
    }//TODO:Make guestView

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

    @Override
    public List<RoomView> seeAllFreeRooms() {
        return null;
    }


}
