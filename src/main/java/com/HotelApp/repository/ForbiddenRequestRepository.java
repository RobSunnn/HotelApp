package com.HotelApp.repository;

import com.HotelApp.domain.entity.ForbiddenRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForbiddenRequestRepository extends JpaRepository<ForbiddenRequestEntity, Long> {
}