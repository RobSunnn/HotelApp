package com.HotelApp.repository;

import com.HotelApp.domain.entity.HotelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelInfoEntity, Long> {

    //todo: maybe it is better to be named hotelInfoRepository
}
