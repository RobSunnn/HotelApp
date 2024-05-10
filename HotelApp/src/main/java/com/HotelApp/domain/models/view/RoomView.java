package com.HotelApp.domain.models.view;

import com.HotelApp.domain.entity.CategoryEntity;

public class RoomView {

    private int roomNumber;

    private CategoryEntity category;

    public RoomView() {}

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomView setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public RoomView setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }
}
