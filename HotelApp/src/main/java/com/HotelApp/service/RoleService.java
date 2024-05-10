package com.HotelApp.service;

import com.HotelApp.domain.entity.RoleEntity;

import java.util.List;

public interface RoleService {

    void initRoles();

    long getCount();

    List<RoleEntity> getAllRoles();
}
