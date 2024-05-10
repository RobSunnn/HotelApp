package com.HotelApp.domain.entity;


import com.HotelApp.domain.entity.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum name;

    public RoleEntity() {
    }

    public RoleEntity(RoleEnum role) {
        this.name = role;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }
}