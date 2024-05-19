package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HappyGuestEntity;
import com.HotelApp.repository.HappyGuestRepository;
import com.HotelApp.service.HappyGuestService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HappyGuestServiceImpl implements HappyGuestService {

    private final HappyGuestRepository happyGuestRepository;

    public HappyGuestServiceImpl(HappyGuestRepository happyGuestRepository) {
        this.happyGuestRepository = happyGuestRepository;
    }

    @Override
    public void saveHappyGuest(HappyGuestEntity happyGuestEntity) {
        happyGuestRepository.save(happyGuestEntity);
    }

    @Override
    public Optional<HappyGuestEntity> findByDocumentId(String documentId) {
        return happyGuestRepository.findByDocumentId(documentId);
    }


}
