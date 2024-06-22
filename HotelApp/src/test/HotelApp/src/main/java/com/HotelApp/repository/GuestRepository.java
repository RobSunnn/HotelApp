package com.HotelApp.repository;

import com.HotelApp.domain.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    GuestEntity findByRoomNumber(int roomNumber);
}
