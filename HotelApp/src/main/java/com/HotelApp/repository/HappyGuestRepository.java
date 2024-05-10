package com.HotelApp.repository;

import com.HotelApp.domain.entity.HappyGuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HappyGuestRepository extends JpaRepository<HappyGuestEntity, Long> {
    Optional<HappyGuestEntity> findByDocumentId(String documentId);
}
