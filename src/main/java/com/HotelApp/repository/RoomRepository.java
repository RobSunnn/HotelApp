package com.HotelApp.repository;

import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    RoomEntity findByRoomNumber(Integer roomNumber);

    List<RoomEntity> findRoomByCategoryName(CategoriesEnum category);

    @Modifying
    @Query(value = "ALTER TABLE rooms AUTO_INCREMENT = 1", nativeQuery = true)
    void resetIdSequence();

}
