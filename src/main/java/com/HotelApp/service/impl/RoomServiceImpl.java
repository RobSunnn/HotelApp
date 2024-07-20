package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CategoryEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.view.RoomView;
import com.HotelApp.repository.CategoriesRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.RoomService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final CategoriesRepository categoriesRepository;

    public RoomServiceImpl(RoomRepository roomRepository, CategoriesRepository categoriesRepository) {
        this.roomRepository = roomRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<RoomView> getAvailableRoomsByType(String roomType) {

        CategoryEntity category = categoriesRepository.findByName(CategoriesEnum.valueOf(roomType));

        return roomRepository.findRoomByCategoryName(category.getName())
                .stream()
                .filter(room -> !room.isReserved())
                .map(this::mapAsRoomView)
                .toList();
    }

    private RoomView mapAsRoomView(RoomEntity room) {
        return new RoomView()
                .setRoomNumber(room.getRoomNumber())
                .setCategory(room.getCategory());
    }

    @Override
    public long getCount() {
        return this.roomRepository.count();
    }

    @Override
    public void initRooms(HotelInfoEntity hotelInfo) {
        Object[][] rooms = {
                {1, 5, BigDecimal.valueOf(50), CategoriesEnum.SINGLE},
                {6, 10, BigDecimal.valueOf(80), CategoriesEnum.STUDIO},
                {11, 15, BigDecimal.valueOf(100), CategoriesEnum.DOUBLE},
                {16, 20, BigDecimal.valueOf(150), CategoriesEnum.DELUXE},
                {21, 22, BigDecimal.valueOf(1250), CategoriesEnum.PRESIDENT}
        };

        for (Object[] room : rooms) {
            int start = (int) room[0];
            int end = (int) room[1];
            BigDecimal price = (BigDecimal) room[2];
            CategoriesEnum category = (CategoriesEnum) room[3];

            for (int i = start; i <= end; i++) {
                RoomEntity roomEntity = new RoomEntity(i, false, price, categoriesRepository.findByName(category), hotelInfo);
                hotelInfo.getRooms().add(roomRepository.save(roomEntity));
            }
        }
    }
}
