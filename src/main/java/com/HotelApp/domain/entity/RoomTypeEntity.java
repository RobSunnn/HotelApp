package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_types")
public class RoomTypeEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, name = "picture_url")
    private String pictureUrl;

    @ManyToOne
    private CategoryEntity category;

    public RoomTypeEntity() {
    }

    public RoomTypeEntity(String name,
                          String description,
                          int capacity,
                          String pictureUrl,
                          CategoryEntity category) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.pictureUrl = pictureUrl;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public RoomTypeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoomTypeEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public RoomTypeEntity setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public RoomTypeEntity setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public RoomTypeEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }
}
