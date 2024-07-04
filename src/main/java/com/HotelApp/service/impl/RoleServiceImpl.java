package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.repository.RoleRepository;
import com.HotelApp.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void initRoles() {
        roleRepository.save(new RoleEntity(RoleEnum.ADMIN));
        roleRepository.save(new RoleEntity(RoleEnum.MODERATOR));
        roleRepository.save(new RoleEntity(RoleEnum.USER));
    }

    @Override
    public long getCount() {
        return roleRepository.count();
    }

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<RoleEntity> getModeratorRole() {
        RoleEntity userRole = getUserRole();
        RoleEntity moderatorRole = getAllRoles()
                .stream()
                .filter(role -> role.getName().name().equals("MODERATOR"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("MODERATOR role not found"));

        return List.of(userRole, moderatorRole);
    }

    @Override
    public RoleEntity getUserRole() {
        return getAllRoles()
                .stream()
                .filter(role -> role.getName().name().equals("USER"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("USER role not found"));
    }
}
