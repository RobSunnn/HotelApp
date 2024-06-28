package com.HotelApp.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {

        // Set up a sample UserEntity
        userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");

        RoleEntity role = new RoleEntity();
        role.setName(RoleEnum.USER);
        userEntity.setRoles(List.of(role));
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = appUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(userEntity.getEmail(), userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            appUserDetailsService.loadUserByUsername("test@example.com");
        });
    }
}