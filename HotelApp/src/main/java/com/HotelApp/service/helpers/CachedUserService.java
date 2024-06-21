package com.HotelApp.service.helpers;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.util.encryptionUtil.KeyStorageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CachedUserService {
    private static final Logger log = LoggerFactory.getLogger(CachedUserService.class);

    private final UserRepository userRepository;

    public CachedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "allUsersCache", key = "'allUsers'")
    public List<UserEntity> findAllUsers() {
        log.info("Fetching users from database");
        return userRepository.findAll().stream().skip(1).toList();
    }
}