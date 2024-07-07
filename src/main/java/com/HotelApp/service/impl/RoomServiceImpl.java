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
        //TODO: better way to init rooms

        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(1, false, BigDecimal.valueOf(50), categoriesRepository.findByName(CategoriesEnum.SINGLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(2, false, BigDecimal.valueOf(50), categoriesRepository.findByName(CategoriesEnum.SINGLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(3, false, BigDecimal.valueOf(50), categoriesRepository.findByName(CategoriesEnum.SINGLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(4, false, BigDecimal.valueOf(50), categoriesRepository.findByName(CategoriesEnum.SINGLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(5, false, BigDecimal.valueOf(50), categoriesRepository.findByName(CategoriesEnum.SINGLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(6, false, BigDecimal.valueOf(80), categoriesRepository.findByName(CategoriesEnum.STUDIO), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(7, false, BigDecimal.valueOf(80), categoriesRepository.findByName(CategoriesEnum.STUDIO), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(8, false, BigDecimal.valueOf(80), categoriesRepository.findByName(CategoriesEnum.STUDIO), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(9, false, BigDecimal.valueOf(80), categoriesRepository.findByName(CategoriesEnum.STUDIO), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(10, false, BigDecimal.valueOf(80), categoriesRepository.findByName(CategoriesEnum.STUDIO), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(11, false, BigDecimal.valueOf(100), categoriesRepository.findByName(CategoriesEnum.DOUBLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(12, false, BigDecimal.valueOf(100), categoriesRepository.findByName(CategoriesEnum.DOUBLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(13, false, BigDecimal.valueOf(100), categoriesRepository.findByName(CategoriesEnum.DOUBLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(14, false, BigDecimal.valueOf(100), categoriesRepository.findByName(CategoriesEnum.DOUBLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(15, false, BigDecimal.valueOf(100), categoriesRepository.findByName(CategoriesEnum.DOUBLE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(16, false, BigDecimal.valueOf(150), categoriesRepository.findByName(CategoriesEnum.DELUXE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(17, false, BigDecimal.valueOf(150), categoriesRepository.findByName(CategoriesEnum.DELUXE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(18, false, BigDecimal.valueOf(150), categoriesRepository.findByName(CategoriesEnum.DELUXE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(19, false, BigDecimal.valueOf(150), categoriesRepository.findByName(CategoriesEnum.DELUXE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(20, false, BigDecimal.valueOf(150), categoriesRepository.findByName(CategoriesEnum.DELUXE), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(21, false, BigDecimal.valueOf(1250), categoriesRepository.findByName(CategoriesEnum.PRESIDENT), hotelInfo)));
        hotelInfo.getRooms().add(roomRepository.save(new RoomEntity(22, false, BigDecimal.valueOf(1250), categoriesRepository.findByName(CategoriesEnum.PRESIDENT), hotelInfo)));

    }


}
