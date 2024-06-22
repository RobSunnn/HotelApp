package com.HotelApp.service;

import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HappyGuestEntity;

import java.util.Optional;

public interface HappyGuestService {

    void saveHappyGuest(HappyGuestEntity happyGuestEntity);

    Optional<HappyGuestEntity> findByDocumentId(GuestEntity guest);

}
