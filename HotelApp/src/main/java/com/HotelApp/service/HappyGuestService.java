package com.HotelApp.service;

import com.HotelApp.domain.entity.HappyGuestEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HappyGuestService {

    void saveHappyGuest(HappyGuestEntity happyGuestEntity);

    Optional<HappyGuestEntity> findByDocumentId(String documentId);

}
