package com.HotelApp.repository;

import com.HotelApp.domain.entity.ContactRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequestEntity, Long> {
}
