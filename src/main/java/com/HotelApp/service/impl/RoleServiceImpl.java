package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.repository.RoleRepository;
import com.HotelApp.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.HotelApp.common.constants.FailConstants.MODERATOR_ROLE_NOT_FOUND;
import static com.HotelApp.common.constants.FailConstants.USER_ROLE_NOT_FOUND;

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
                .filter(role -> role.getName().name().equals(RoleEnum.MODERATOR.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(MODERATOR_ROLE_NOT_FOUND));

        return List.of(userRole, moderatorRole);
    }

    @Override
    public RoleEntity getUserRole() {
        return getAllRoles()
                .stream()
                .filter(role -> role.getName().name().equals(RoleEnum.USER.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(USER_ROLE_NOT_FOUND));
    }
}
