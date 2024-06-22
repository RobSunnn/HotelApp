package com.HotelApp.domain.models.view;

import com.HotelApp.domain.entity.CategoryEntity;

import java.math.BigDecimal;

public class RoomView {

    private Long id;

    private int roomNumber;

    private BigDecimal roomPrice;

    private CategoryEntity category;

    private boolean isReserved;

    public RoomView() {
    }

    public Long getId() {
        return id;
    }

    public RoomView setId(Long id) {
        this.id = id;
        return this;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomView setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public RoomView setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public RoomView setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public RoomView setReserved(boolean reserved) {
        isReserved = reserved;
        return this;
    }
}
