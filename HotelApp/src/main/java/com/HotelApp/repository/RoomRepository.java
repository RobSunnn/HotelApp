package com.HotelApp.repository;

import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query(value = "SELECT * FROM rooms where is_reserved = 0", nativeQuery = true)
    List<RoomEntity> findAllNotReservedRooms();

    RoomEntity findByRoomNumber(Integer roomNumber);

    @Query(value = "SELECT * FROM hotel_app.rooms where category_id = 1 and is_reserved = 0", nativeQuery = true)
    List<RoomEntity> findRoomByCategoryId(Long id);

    List<RoomEntity> findRoomByCategoryName(CategoriesEnum category);
}
