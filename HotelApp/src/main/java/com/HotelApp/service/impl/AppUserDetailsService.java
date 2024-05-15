package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.service.CustomUserDetails;
import com.HotelApp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(AppUserDetailsService::mapFromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
    }

    private static UserDetails mapFromEntity(UserEntity userEntity) {
        // Create a custom UserDetails implementation
        CustomUserDetails userDetails = new CustomUserDetails(
                userEntity.getEmail(), // Use email as username
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(AppUserDetailsService::mapRoles).toList()
        );
        userDetails.setFullName(userEntity.getFullName()); // Set the full name
        return userDetails;
    }

    private static GrantedAuthority mapRoles(RoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getName().name());
    }

}
