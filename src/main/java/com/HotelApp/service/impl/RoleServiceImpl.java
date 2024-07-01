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
}
