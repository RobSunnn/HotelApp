package com.HotelApp.domain.entity;

import com.HotelApp.domain.entity.enums.CategoriesEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "room_categories")
public class CategoryEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriesEnum name;

    public CategoryEntity() {

    }

    public CategoryEntity(CategoriesEnum name) {
        this.name = name;
    }

    public CategoriesEnum getName() {
        return name;
    }

    public CategoryEntity setName(CategoriesEnum name) {
        this.name = name;
        return this;
    }

}
