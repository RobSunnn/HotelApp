package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.service.CustomUser;
import com.HotelApp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
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

        return new CustomUser(
                userEntity.getEmail(), // Use email as username
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(AppUserDetailsService::mapRoles)
                        .toList(),
                userEntity.getFullName()
        );
    }

    private static GrantedAuthority mapRoles(RoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getName().name());
    }

}
