package com.HotelApp.repository;

import com.HotelApp.domain.entity.OnlineReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineReservationRepository extends JpaRepository<OnlineReservationEntity, Long> {
}
