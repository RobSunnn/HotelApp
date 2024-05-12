package com.HotelApp.domain.models.view;

import com.HotelApp.domain.entity.CategoryEntity;

public class RoomTypeView {

    private String name;

    private String description;

    private Integer capacity;

    private CategoryEntity category;

    private String pictureUrl;

    public RoomTypeView() {
    }

    public String getName() {
        return name;
    }

    public RoomTypeView setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoomTypeView setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public RoomTypeView setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public RoomTypeView setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public RoomTypeView setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
