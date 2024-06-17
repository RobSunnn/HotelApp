package com.HotelApp.service.helpers;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CachedUserService {

    private final UserRepository userRepository;

    public CachedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("allUsersCache")
    public List<UserEntity> findAllUsers() {
        System.out.println("CACHE USERS HERe");
        return userRepository.findAll();
    }
}